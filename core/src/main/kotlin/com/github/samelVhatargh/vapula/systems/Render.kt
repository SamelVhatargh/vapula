package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.RENDERABLE_FAMILY
import ktx.ashley.get
import ktx.graphics.use

class Render(private val batch: SpriteBatch, viewport: Viewport, private val player: Entity) :
    SortedIteratingSystem(RENDERABLE_FAMILY, compareBy { entity -> entity[Graphics.mapper] }) {

    private val camera = viewport.camera

    override fun update(deltaTime: Float) {
        forceSort()
        batch.use(camera.combined) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!
        val fov = player[FieldOfView.mapper]!!

        if (!fov.isVisible(position)) return

        val graphics = entity[Graphics.mapper]!!

        graphics.sprite.setPosition(position.x.toFloat(), position.y.toFloat())
        graphics.sprite.draw(batch)
    }
}