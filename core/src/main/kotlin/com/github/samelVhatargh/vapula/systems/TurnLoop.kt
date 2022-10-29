package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.components.*
import ktx.ashley.*
import ktx.log.logger

class TurnLoop(private val player: Entity) :
    IteratingSystem(
        allOf(Action::class).exclude(Dead::class).get()
    ) {

    companion object {
        val log = logger<TurnLoop>()
    }

    override fun update(deltaTime: Float) {
        val playerAction = player[Action.mapper]
        playerAction?.let {
            super.update(deltaTime)
            engine.getSystem<Ai>().setProcessing(true)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[Action.mapper]!!.command.execute()
        entity.remove<Action>()
    }
}
