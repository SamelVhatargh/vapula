package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.components.*
import ktx.ashley.*
import ktx.log.logger

private const val AUTO_TURN_DELAY = .1f

class TurnLoop(private val player: Entity) :
    IteratingSystem(
        allOf(Action::class).exclude(Dead::class).get()
    ) {

    private var delay = AUTO_TURN_DELAY

    companion object {
        val log = logger<TurnLoop>()
    }

    override fun update(deltaTime: Float) {
        if (delay < AUTO_TURN_DELAY) {
            delay += deltaTime
            return
        }

        val playerAction = player[Action.mapper]
        playerAction?.let {
            super.update(deltaTime)
            engine.getSystem<Ai>().setProcessing(true)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val shouldBeRepeatedNextTurn = entity[Action.mapper]!!.command.execute()
        if (!shouldBeRepeatedNextTurn) {
            entity.remove<Action>()
        } else if (entity.has(Player.mapper)) {
            delay = 0f
        }
    }
}
