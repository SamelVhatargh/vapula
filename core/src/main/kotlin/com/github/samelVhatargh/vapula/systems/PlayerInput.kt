package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.systems.commands.AggressiveMove
import com.github.samelVhatargh.vapula.systems.commands.DoNothing
import com.github.samelVhatargh.vapula.systems.commands.GoDownstairs
import com.github.samelVhatargh.vapula.systems.commands.GoUpstairs
import ktx.app.KtxInputAdapter
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.oneOf

class PlayerInput(private val inputMultiplexer: InputMultiplexer, private val world: World) :
    EntitySystem(), KtxInputAdapter {

    private val playerEntity = world.player
    private val player = playerEntity[Player.mapper]!!

    override fun addedToEngine(engine: Engine) {
        inputMultiplexer.addProcessor(this)
    }

    override fun removedFromEngine(engine: Engine) {
        inputMultiplexer.removeProcessor(this)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (player.command !== null) return true

        when (keycode) {
            Input.Keys.U -> useStairs()
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

    private fun useStairs() {
        val entity =
            engine.getEntityAtPosition(playerEntity[Position.mapper]!!, oneOf(GoUp::class, GoDown::class).get())

        if (entity !== null) {
            if (entity.has(GoUp.mapper)) {
                player.command = GoUpstairs(engine, world, playerEntity)
                return
            }
            if (entity.has(GoDown.mapper)) {
                player.command = GoDownstairs(engine, world, playerEntity)
                return
            }
        }
    }

    private fun move(direction: Direction) {
        if (playerEntity.has(Dead.mapper)) {
            doNothing()
            return
        }
        player.command = AggressiveMove(engine, playerEntity, direction, world.storey)
    }

    private fun doNothing() {
        player.command = DoNothing()
    }
}