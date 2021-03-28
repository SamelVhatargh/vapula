package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.GameState
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.systems.commands.Attack
import com.github.samelVhatargh.vapula.systems.commands.Move
import ktx.ashley.*
import ktx.log.logger

class EnemyTurns(private val gameState: GameState, private val pathFinder: PathFinder) : IteratingSystem(
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

    private val player: Entity by lazy {
        engine.getEntitiesFor(allOf(Player::class).get()).first()
    }

    companion object {
        val log = logger<EnemyTurns>()
    }

    override fun update(deltaTime: Float) {
        if (gameState.isPlayerTurn) return
        super.update(deltaTime)
        gameState.isPlayerTurn = true
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        //if player is near - attack!
        val playerPosition = player[Position.mapper]!!
        val monsterPosition = entity[Position.mapper]!!

        if (playerPosition.isNeighbourTo(monsterPosition) && !player.has(Dead.mapper)) {
            engine.getSystem<Attack>().execute(entity, player)
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

        engine.getSystem<Move>().execute(entity, path)
    }

    private fun wander(entity: Entity) {
        //wander
        val direction = directions.random()
        engine.getSystem<Move>().execute(entity, direction)
        return
    }
}
