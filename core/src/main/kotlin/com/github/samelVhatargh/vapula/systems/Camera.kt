package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Direction
import ktx.app.KtxInputAdapter
import ktx.ashley.allOf
import ktx.ashley.get

private const val CAMERA_MOUSE_MOVE_SPEED = 7.5f
private const val EDGE_SIZE = 32f

/**
 * Управление камерой
 */
class Camera(private val camera: Camera, private val inputMultiplexer: InputMultiplexer) :
    IteratingSystem(allOf(Player::class, Position::class).get()),
    KtxInputAdapter {

    var moveWithMouseEnabled = false
        set(value) {
            field = value
            if (field) {
                inputMultiplexer.addProcessor(this)
                return
            }

            inputMultiplexer.removeProcessor(this)
        }

    private var cameraMoveDirection = Direction.NONE

    override fun update(deltaTime: Float) {
        if (moveWithMouseEnabled) {
            moveCameraInMouseDirection(deltaTime)
        } else {
            super.update(deltaTime)
        }
    }

    private fun moveCameraInMouseDirection(deltaTime: Float) {
        if (cameraMoveDirection === Direction.NONE) {
            return
        }

        camera.position.x += cameraMoveDirection.x * deltaTime * CAMERA_MOUSE_MOVE_SPEED
        camera.position.y += cameraMoveDirection.y * deltaTime * CAMERA_MOUSE_MOVE_SPEED
        camera.update()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!

        camera.position.set(position)
        camera.update()
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val width = Gdx.graphics.width - EDGE_SIZE
        val height = Gdx.graphics.height - EDGE_SIZE

        cameraMoveDirection = when {
            screenX <= EDGE_SIZE -> when {
                screenY <= EDGE_SIZE -> Direction.NORTH_WEST
                screenY >= height -> Direction.SOUTH_WEST
                else -> Direction.WEST
            }
            screenX >= width -> when {
                screenY <= EDGE_SIZE -> Direction.NORTH_EAST
                screenY >= height -> Direction.SOUTH_EAST
                else -> Direction.EAST
            }
            screenY <= EDGE_SIZE -> Direction.NORTH
            screenY >= height -> Direction.SOUTH
            else -> Direction.NONE
        }

        camera.update()

        return true
    }
}

private fun Vector3.set(position: Position) {
    set(position.x.toFloat(), position.y.toFloat(), 0f)
}
