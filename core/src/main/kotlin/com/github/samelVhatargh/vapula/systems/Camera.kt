package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.allOf
import ktx.ashley.get

/**
 * Центрирует камеру на игроке
 */
class Camera(private val camera: Camera) : IteratingSystem(
    allOf(Player::class, Position::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!

        camera.position.set(position)
        camera.update()
    }
}

private fun Vector3.set(position: Position) {
    set(position.x.toFloat(), position.y.toFloat(), 0f)
}
