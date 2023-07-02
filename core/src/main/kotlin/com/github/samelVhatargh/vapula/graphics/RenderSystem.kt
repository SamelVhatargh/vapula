package com.github.samelVhatargh.vapula.graphics

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.game.World
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.entities.RENDERABLE_FAMILY
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.FieldOfViewComponent
import com.github.samelVhatargh.vapula.map.PositionComponent
import ktx.ashley.get
import ktx.ashley.has
import ktx.graphics.use

class RenderSystem(
    private val spriteCache: SpriteCache,
    private val batch: SpriteBatch,
    viewport: Viewport,
    private val world: World
) :
    SortedIteratingSystem(RENDERABLE_FAMILY, compareBy { entity -> entity[GraphicsComponent.mapper] }) {

    private val player = world.player
    private val camera = viewport.camera

    override fun update(deltaTime: Float) {
        forceSort()
        batch.use(camera.combined) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[PositionComponent.mapper]!!
        val graphics = entity[GraphicsComponent.mapper]!!
        val dead = entity.has(Dead.mapper)

        if (position.z != world.storey.z) return

        val fov = player[FieldOfViewComponent.mapper]!!
        if (!fov.isVisible(position)) return

        val vector = graphics.position ?: position.toVec2()
        spriteCache.getSprite(graphics).apply {
            setPosition(vector.x, vector.y)
            setOrigin(.5f, 0f)
            setScale(graphics.scale.x, graphics.scale.y)
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

            setFlip(graphics.direction === Direction.EAST, false)

            draw(batch)
        }
    }

}