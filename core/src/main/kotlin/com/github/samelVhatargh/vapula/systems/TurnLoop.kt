package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.AiBrain
import com.github.samelVhatargh.vapula.components.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.logger

class TurnLoop(private val aiBrain: AiBrain, private val player: Entity) :
    IteratingSystem(
        allOf(Ai::class, Name::class, Position::class).exclude(Dead::class).get()
    ) {

    companion object {
        val log = logger<TurnLoop>()
    }

    override fun update(deltaTime: Float) {
        val playerCommand = player[Player.mapper]!!
        playerCommand.command?.let {
            it.execute()
            super.update(deltaTime)
            playerCommand.command = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        aiBrain.getCommand(entity).execute()
    }
}
