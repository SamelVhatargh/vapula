package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.GameState
import com.github.samelVhatargh.vapula.components.Ai
import com.github.samelVhatargh.vapula.components.Name
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger

class EnemyTurns(private val gameState: GameState) : IteratingSystem(
    allOf(Ai::class, Name::class).get()
) {

    companion object {
        val log = logger<EnemyTurns>()
    }

    override fun update(deltaTime: Float) {
        if (gameState.isPlayerTurn) return
        super.update(deltaTime)
        gameState.isPlayerTurn = true
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        log.debug { "${entity[Name.mapper]!!.name} acts" }
    }
}
