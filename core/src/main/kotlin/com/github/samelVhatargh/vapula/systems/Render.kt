package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.entities.RENDERABLE_FAMILY
import com.github.samelVhatargh.vapula.utility.SpriteCache
import ktx.ashley.get
import ktx.ashley.remove
import ktx.graphics.use
import ktx.math.vec2

class Render(
    private val spriteCache: SpriteCache,
    private val batch: SpriteBatch,
    viewport: Viewport,
    private val world: World
) :
    SortedIteratingSystem(RENDERABLE_FAMILY, compareBy { entity -> entity[Graphics.mapper] }) {

    private val player = world.player
    private val camera = viewport.camera

    override fun update(deltaTime: Float) {
        forceSort()
        batch.use(camera.combined) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!
        if (position.z != world.storey.z) {
            entity.remove<Animation>()
            return
        }

        val fov = player[FieldOfView.mapper]!!
        if (!fov.isVisible(position)) {
            entity.remove<Animation>()
            return
        }

        val vector = animate(entity, deltaTime) ?: vec2(position.x.toFloat(), position.y.toFloat())
        spriteCache.getSprite(entity[Graphics.mapper]!!).apply {
            setPosition(vector.x, vector.y)
            draw(batch)
        }
    }

    private fun animate(entity: Entity, deltaTime: Float): Vector2? {
        val animation = entity[Animation.mapper] ?: return null

        if (animation.type === AnimationType.WALK) {
            val speed = 3f

            animation.vector.lerp(vec2(animation.end.x.toFloat(), animation.end.y.toFloat()), animation.progress)
            animation.progress += deltaTime * speed

            if (animation.progress >= 1f) {
                entity.remove<Animation>()
            }
        }

        return animation.vector
    }
}