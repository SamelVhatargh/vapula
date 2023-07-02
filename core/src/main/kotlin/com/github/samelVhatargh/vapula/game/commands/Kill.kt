package com.github.samelVhatargh.vapula.game.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.events.EntityDied
import com.github.samelVhatargh.vapula.events.NotifierInterface
import com.github.samelVhatargh.vapula.game.statuses.Dead
import com.github.samelVhatargh.vapula.graphics.AnimationComponent
import com.github.samelVhatargh.vapula.graphics.GraphicsComponent
import com.github.samelVhatargh.vapula.graphics.IdleAnimationComponent
import com.github.samelVhatargh.vapula.graphics.Layer
import ktx.ashley.get
import ktx.ashley.plusAssign
import ktx.ashley.remove
import ktx.math.vec2

/**
 * Kills entity
 */
class Kill(private val notifier: NotifierInterface, private val entity: Entity) : Command {
    override fun execute(): Boolean {
        val graphics = entity[GraphicsComponent.mapper]!!

        entity += Dead()
        entity.remove<AnimationComponent>()
        entity.remove<IdleAnimationComponent>()
        graphics.scale = vec2(1f, 1f)
        graphics.layer = Layer.CORPSE

        notifier.notify(EntityDied(entity))

        return false
    }
}