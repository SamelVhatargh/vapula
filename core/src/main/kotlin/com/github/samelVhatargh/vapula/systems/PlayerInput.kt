package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.github.samelVhatargh.vapula.GameState
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.systems.commands.Move
import com.github.samelVhatargh.vapula.systems.commands.MoveDirection
import ktx.app.KtxInputAdapter
import ktx.ashley.get
import ktx.ashley.getSystem
import ktx.math.vec3

class PlayerInput(
    private val player: Entity,
    map: Entity,
    private val camera: Camera,
    private val gameState: GameState
) : EntitySystem(), KtxInputAdapter {

    private val gameMap = map[GameMap.mapper]!!

    override fun addedToEngine(engine: Engine) {
        Gdx.input.inputProcessor = this
    }

    override fun removedFromEngine(engine: Engine) {
        Gdx.input.inputProcessor = null
    }

    override fun keyDown(keycode: Int): Boolean {
        if (!gameState.isPlayerTurn) return true

        when (keycode) {
            Input.Keys.UP, Input.Keys.NUMPAD_8 -> move(MoveDirection.NORTH)
            Input.Keys.DOWN, Input.Keys.NUMPAD_2 -> move(MoveDirection.SOUTH)
            Input.Keys.LEFT, Input.Keys.NUMPAD_4 -> move(MoveDirection.WEST)
            Input.Keys.RIGHT, Input.Keys.NUMPAD_6 -> move(MoveDirection.EAST)
            Input.Keys.NUMPAD_7 -> move(MoveDirection.NORTH_WEST)
            Input.Keys.NUMPAD_9 -> move(MoveDirection.NORTH_EAST)
            Input.Keys.NUMPAD_1 -> move(MoveDirection.SOUTH_WEST)
            Input.Keys.NUMPAD_3 -> move(MoveDirection.SOUTH_EAST)
            Input.Keys.NUMPAD_5 -> doNothing()
        }

        return true
    }

    private fun move(direction: MoveDirection) {
        engine.getSystem<Move>().execute(player, direction)
        gameState.isPlayerTurn = false
    }

    private fun doNothing() {
        gameState.isPlayerTurn = false

    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val position = camera.unproject(vec3(screenX.toFloat(), screenY.toFloat()))

        if (button == Input.Buttons.LEFT) {
            gameMap.switchTile(position.x.toInt(), position.y.toInt())
        }

        return true
    }
}