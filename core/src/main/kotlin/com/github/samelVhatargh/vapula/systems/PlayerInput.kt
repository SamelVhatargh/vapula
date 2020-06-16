package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Input
import com.github.samelVhatargh.vapula.components.*
import ktx.app.KtxInputAdapter
import ktx.ashley.has
import ktx.ashley.plusAssign

class PlayerInput(private val player: Entity) : EntitySystem(), KtxInputAdapter {

    override fun keyDown(keycode: Int): Boolean {
        if (!player.has(MoveDirection.mapper)) {
            val move = MoveDirection()
            when (keycode) {
                Input.Keys.UP, Input.Keys.NUMPAD_8 -> move.direction.set(DIRECTION_NONE, DIRECTION_UP)
                Input.Keys.DOWN, Input.Keys.NUMPAD_2 -> move.direction.set(DIRECTION_NONE, DIRECTION_DOWN)
                Input.Keys.LEFT, Input.Keys.NUMPAD_4 -> move.direction.set(DIRECTION_LEFT, DIRECTION_NONE)
                Input.Keys.RIGHT, Input.Keys.NUMPAD_6 -> move.direction.set(DIRECTION_RIGHT, DIRECTION_NONE)
                Input.Keys.NUMPAD_7 -> move.direction.set(DIRECTION_LEFT, DIRECTION_UP)
                Input.Keys.NUMPAD_9 -> move.direction.set(DIRECTION_RIGHT, DIRECTION_UP)
                Input.Keys.NUMPAD_1 -> move.direction.set(DIRECTION_LEFT, DIRECTION_DOWN)
                Input.Keys.NUMPAD_3 -> move.direction.set(DIRECTION_RIGHT, DIRECTION_DOWN)
            }

            player += move
        }

        return true
    }

}