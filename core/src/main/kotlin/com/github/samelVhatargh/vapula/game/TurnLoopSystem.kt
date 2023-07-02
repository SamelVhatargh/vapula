package com.github.samelVhatargh.vapula.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.game.ai.AiSystem
import com.github.samelVhatargh.vapula.game.commands.ActionComponent
import com.github.samelVhatargh.vapula.game.statuses.Dead
import ktx.ashley.*
import ktx.log.logger

private const val AUTO_TURN_DELAY = .1f

class TurnLoopSystem(private val player: Entity) :
    IteratingSystem(
        allOf(ActionComponent::class).exclude(Dead::class).get()
    ) {

    private var delay = AUTO_TURN_DELAY

    companion object {
        val log = logger<TurnLoopSystem>()
    }

    override fun update(deltaTime: Float) {
        if (delay < AUTO_TURN_DELAY) {
            delay += deltaTime
            return
        }

        val playerAction = player[ActionComponent.mapper]
        playerAction?.let {
            if (player.hasNot(Dead.mapper)) {
                super.update(deltaTime)
            }
            engine.getSystem<AiSystem>().setProcessing(true)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val shouldBeRepeatedNextTurn = entity[ActionComponent.mapper]!!.command.execute()
        if (!shouldBeRepeatedNextTurn) {
            entity.remove<ActionComponent>()
        } else if (entity.has(Player.mapper)) {
            delay = 0f
        }
    }
}
