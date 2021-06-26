package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.systems.commands.Attack
import com.github.samelVhatargh.vapula.systems.commands.MoveInDirection
import com.github.samelVhatargh.vapula.systems.commands.MoveInPath
import com.github.samelVhatargh.vapula.systems.commands.effects.Effect
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.ashley.has
import ktx.log.logger

class TurnLoop(private val pathFinder: PathFinder, world: World) :
    IteratingSystem(
        allOf(Ai::class, Name::class, Position::class).exclude(Dead::class).get()
    ) {

    private val directions = listOf(
        Direction.NORTH,
        Direction.SOUTH,
        Direction.EAST,
        Direction.WEST,
        Direction.NORTH_EAST,
        Direction.NORTH_WEST,
        Direction.SOUTH_EAST,
        Direction.SOUTH_WEST
    )

    private val player = world.player
    private val gameMap = world.gameMap

    companion object {
        val log = logger<TurnLoop>()
    }

    override fun update(deltaTime: Float) {
        val playerCommand = player[Player.mapper]!!
        playerCommand.command?.let {
            applyEffects(it.execute())
            super.update(deltaTime)
            playerCommand.command = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        //if player is near - attack!
        val playerPosition = player[Position.mapper]!!
        val monsterPosition = entity[Position.mapper]!!

        if (playerPosition.isNeighbourTo(monsterPosition) && !player.has(Dead.mapper)) {
            applyEffects(Attack(engine.notifier, entity, player).execute())
            return
        }

        if (player.has(Dead.mapper)) {
            wander(entity)
            return
        }

        //else run to player
        val path = pathFinder.findPath(monsterPosition, playerPosition)
        if (path.isEmpty()) {
            wander(entity)
            return
        }

        applyEffects(MoveInPath(engine, entity, path, gameMap).execute())
    }

    private fun wander(entity: Entity) {
        //wander
        val direction = directions.random()
        applyEffects(MoveInDirection(engine, entity, direction, gameMap).execute())
    }

    private fun applyEffects(effects: Array<Effect>) {
        effects.forEach {
            val subEffects = it.apply()
            applyEffects(subEffects)
        }
    }
}
