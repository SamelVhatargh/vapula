package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.github.samelVhatargh.vapula.GameState
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.systems.commands.MoveOrAttack
import ktx.app.KtxInputAdapter
import ktx.ashley.getSystem
import ktx.ashley.has

class PlayerInput(
    private val inputMultiplexer: InputMultiplexer,
    private val player: Entity,
    private val gameState: GameState
) : EntitySystem(), KtxInputAdapter {

    override fun addedToEngine(engine: Engine) {
        inputMultiplexer.addProcessor(this)
    }

    override fun removedFromEngine(engine: Engine) {
        inputMultiplexer.removeProcessor(this)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (!gameState.isPlayerTurn) return true

        when (keycode) {
            Input.Keys.UP, Input.Keys.NUMPAD_8 -> move(Direction.NORTH)
            Input.Keys.DOWN, Input.Keys.NUMPAD_2 -> move(Direction.SOUTH)
            Input.Keys.LEFT, Input.Keys.NUMPAD_4 -> move(Direction.WEST)
            Input.Keys.RIGHT, Input.Keys.NUMPAD_6 -> move(Direction.EAST)
            Input.Keys.NUMPAD_7 -> move(Direction.NORTH_WEST)
            Input.Keys.NUMPAD_9 -> move(Direction.NORTH_EAST)
            Input.Keys.NUMPAD_1 -> move(Direction.SOUTH_WEST)
            Input.Keys.NUMPAD_3 -> move(Direction.SOUTH_EAST)
            Input.Keys.NUMPAD_5 -> doNothing()
        }

        return true
    }

    private fun move(direction: Direction) {
        if (player.has(Dead.mapper)) {
            doNothing()
            return
        }
        engine.getSystem<MoveOrAttack>().execute(player, direction)
        gameState.isPlayerTurn = false
    }

    private fun doNothing() {
        gameState.isPlayerTurn = false
    }
}