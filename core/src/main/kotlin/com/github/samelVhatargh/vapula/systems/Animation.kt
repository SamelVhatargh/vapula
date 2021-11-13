package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.Animation
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.ANIMATION_FAMILY
import ktx.ashley.get
import ktx.ashley.remove
import ktx.math.vec2

/**
 * This system updates entity [Graphics.position] according to its current [Animation]
 */
class Animation(private val world: World) : IteratingSystem(ANIMATION_FAMILY) {

    private val player = world.player

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!
        val graphics = entity[Graphics.mapper]!!
        if (position.z != world.storey.z) {
            removeAnimation(entity)
            return
        }

        val fov = player[FieldOfView.mapper]!!
        if (!fov.isVisible(position)) {
            removeAnimation(entity)
            return
        }

        graphics.position = animate(entity, deltaTime)
    }

    private fun animate(entity: Entity, deltaTime: Float): Vector2? {
        val animation = entity[Animation.mapper] ?: return null

        val speed = animation.description.speed

        val transition = animation.transition
        if (transition === null) {
            removeAnimation(entity)
            return null
        }

        animation.animatedVector = vec2(animation.startVector.x, animation.startVector.y)
        animation.animatedVector.lerp(transition.point, transition.progress)

        transition.progress += (deltaTime / speed) * animation.description.transitionProgressFactor
        animation.progress += (deltaTime / speed)

        if (animation.progress >= 1f) {
            removeAnimation(entity)
            return null
        }

        return animation.animatedVector
    }

    private fun removeAnimation(entity: Entity) {
        val animation = entity.remove<Animation>()
        entity[Graphics.mapper]!!.position = null
        if (animation is Animation && animation.description.destroyEntityOnComplete) {
            engine.removeEntity(entity)
        }
    }
}