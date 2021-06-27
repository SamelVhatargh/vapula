package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.systems.commands.Attack
import com.github.samelVhatargh.vapula.systems.commands.Command
import com.github.samelVhatargh.vapula.systems.commands.MoveInDirection
import com.github.samelVhatargh.vapula.systems.commands.MoveInPath
import ktx.ashley.get
import ktx.ashley.has

/**
 * Decides what a monster will do
 */
class AiBrain(private val engine: Engine, world: World) {
    private val player = world.player
    private val gameMap = world.gameMap
    private val pathFinder = PathFinder(gameMap, engine)

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

    fun getCommand(entity: Entity): Command {
        val playerPosition = player[Position.mapper]!!
        val monsterPosition = entity[Position.mapper]!!

        if (player.has(Dead.mapper)) {
            return wander(entity)
        }

        //if player is near - attack!
        if (playerPosition.isNeighbourTo(monsterPosition)) {
            return Attack(engine.notifier, entity, player)
        }

        //else run to player
        val path = pathFinder.findPath(monsterPosition, playerPosition)
        if (path.isEmpty()) {
            return wander(entity)
        }

        return MoveInPath(engine, entity, path, gameMap)
    }

    private fun wander(entity: Entity): Command = MoveInDirection(engine, entity, directions.random(), gameMap)
}