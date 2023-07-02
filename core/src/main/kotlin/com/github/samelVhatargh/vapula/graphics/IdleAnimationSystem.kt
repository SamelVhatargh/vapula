package com.github.samelVhatargh.vapula.graphics

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get

private const val IDLE_THRESHOLD = .25f

/**
 * Changes [GraphicsComponent.scale] depending on current [IdleAnimationComponent.progress]
 */
class IdleAnimationSystem : IteratingSystem(allOf(GraphicsComponent::class, IdleAnimationComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animation = entity[IdleAnimationComponent.mapper]!!
        val graphics = entity[GraphicsComponent.mapper]!!

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