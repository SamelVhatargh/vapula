package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class Render(private val batch: SpriteBatch) : IteratingSystem(
    allOf(Position::class, Graphics::class).get()
) {

    override fun update(deltaTime: Float) {
        batch.use {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!
        val graphics = entity[Graphics.mapper]!!

        graphics.sprite.x = position.x.toFloat()
        graphics.sprite.y = position.y.toFloat()
        graphics.sprite.draw(batch)
    }
}