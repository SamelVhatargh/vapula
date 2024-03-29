package com.github.samelVhatargh.vapula.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.github.samelVhatargh.vapula.graphics.AnimationComponent
import com.github.samelVhatargh.vapula.game.PlayerComponent
import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.map.Direction
import ktx.app.KtxInputAdapter
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.update

private const val CAMERA_MOUSE_MOVE_SPEED = 7.5f
private const val EDGE_SIZE = 32f
private const val ZOOM_SPEED = 0.5f

/**
 * Управление камерой
 */
class CameraSystem(private val camera: OrthographicCamera, private val inputMultiplexer: InputMultiplexer) : EntitySystem(),
    KtxInputAdapter {

    var moveWithMouseEnabled = false
        set(value) {
            field = value
            if (field) {
                inputMultiplexer.addProcessor(this)
                return
            }

            camera.zoom = 1f
            inputMultiplexer.removeProcessor(this)
        }

    private var cameraMoveDirection = Direction.NONE

    private lateinit var player: Entity

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        player = engine.getEntitiesFor(allOf(PlayerComponent::class, PositionComponent::class).get()).first()
    }

    override fun update(deltaTime: Float) {
        camera.update {
            if (moveWithMouseEnabled) {
                moveCameraInMouseDirection(deltaTime)
            } else {
                centerCameraOnPlayer()
            }
        }
    }

    private fun moveCameraInMouseDirection(deltaTime: Float) {
        if (cameraMoveDirection === Direction.NONE) {
            return
        }

        camera.position.x += cameraMoveDirection.x * deltaTime * CAMERA_MOUSE_MOVE_SPEED * camera.zoom
        camera.position.y += cameraMoveDirection.y * deltaTime * CAMERA_MOUSE_MOVE_SPEED * camera.zoom
    }

    private fun centerCameraOnPlayer() {
        val position = player[PositionComponent.mapper]!!
        var x = position.x.toFloat()
        var y = position.y.toFloat()

        val animation = player[AnimationComponent.mapper]
        if (animation != null && animation.description.moveCamera) {
            x = animation.animatedVector.x
            y = animation.animatedVector.y
        }

        camera.position.x = x + HUD_WIDTH / 2
        camera.position.y = y
    }

    /**
     * Обновляет [cameraMoveDirection]
     */
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

        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        camera.zoom += ZOOM_SPEED * amountY
        return true
    }
}
