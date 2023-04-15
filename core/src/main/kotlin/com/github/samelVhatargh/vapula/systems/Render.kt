package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.entities.RENDERABLE_FAMILY
import com.github.samelVhatargh.vapula.utility.SpriteCache
import ktx.ashley.get
import ktx.ashley.has
import ktx.graphics.use

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
        val graphics = entity[Graphics.mapper]!!
        val dead = entity.has(Dead.mapper)

        if (position.z != world.storey.z) return

        val fov = player[FieldOfView.mapper]!!
        if (!fov.isVisible(position)) return

        val vector = graphics.position ?: position.toVec2()
        spriteCache.getSprite(graphics).apply {
            setPosition(vector.x, vector.y)
            setScale(1f)
            rotation = 0f

            val spriteRotation = graphics.rotation
            if (spriteRotation != null) {
                setOriginCenter()
                rotation = spriteRotation
            }

            if (dead) {
                setOriginCenter()
                rotation = -90f
                setScale(.5f, 1f)
                setPosition(vector.x, vector.y - .25f)
                setPosition(vector.x, vector.y - .25f)
            }
            draw(batch)
        }
    }

}