package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.GameState
import com.github.samelVhatargh.vapula.components.Ai
import com.github.samelVhatargh.vapula.components.Name
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.systems.commands.Move
import com.github.samelVhatargh.vapula.systems.commands.MoveDirection
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.log.logger

class EnemyTurns(private val gameState: GameState) : IteratingSystem(
    allOf(Ai::class, Name::class, Position::class).get()
) {

    private val directions = listOf(
        MoveDirection.NORTH,
        MoveDirection.SOUTH,
        MoveDirection.EAST,
        MoveDirection.WEST,
        MoveDirection.NORTH_EAST,
        MoveDirection.NORTH_WEST,
        MoveDirection.SOUTH_EAST,
        MoveDirection.SOUTH_WEST
    )

    companion object {
        val log = logger<EnemyTurns>()
    }

    override fun update(deltaTime: Float) {
        if (gameState.isPlayerTurn) return
        super.update(deltaTime)
        gameState.isPlayerTurn = true
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val direction = directions.random()

        engine.getSystem<Move>().execute(entity, direction)
    }
}
