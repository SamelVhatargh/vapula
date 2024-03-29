package com.github.samelVhatargh.vapula.game.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.game.commands.ActionComponent
import com.github.samelVhatargh.vapula.game.statuses.DeadComponent
import com.github.samelVhatargh.vapula.game.statuses.InDangerComponent
import com.github.samelVhatargh.vapula.game.stats.NameComponent
import com.github.samelVhatargh.vapula.map.PositionComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.plusAssign
import ktx.ashley.remove

class AiSystem(private val aiBrain: AiBrain, private val player: Entity) :
    IteratingSystem(allOf(AiComponent::class, NameComponent::class, PositionComponent::class).exclude(DeadComponent::class).get()) {

    override fun update(deltaTime: Float) {
        player.remove<InDangerComponent>()
        super.update(deltaTime)
        setProcessing(false)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val command = aiBrain.getCommand(entity)
        entity += ActionComponent(command)
    }

}