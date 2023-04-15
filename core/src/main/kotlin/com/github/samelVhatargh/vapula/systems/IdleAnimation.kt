package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.IdleAnimation
import ktx.ashley.allOf
import ktx.ashley.get

const val IDLE_THRESHOLD = .25f

/**
 * Changes [Graphics.scale] depending on current [IdleAnimation.progress]
 */
class IdleAnimation : IteratingSystem(allOf(Graphics::class, IdleAnimation::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animation = entity[IdleAnimation.mapper]!!
        val graphics = entity[Graphics.mapper]!!

        graphics.scale.y = 1f
        if (animation.progress <= animation.length * IDLE_THRESHOLD) {
            graphics.scale.y = .98f
        }

        if (animation.progress > animation.length) {
            animation.progress = 0f
        }

        animation.progress += deltaTime
    }
}