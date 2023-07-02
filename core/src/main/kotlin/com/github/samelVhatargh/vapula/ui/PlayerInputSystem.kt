package com.github.samelVhatargh.vapula.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.game.commands.*
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.Path
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.map.PositionComponent
import ktx.app.KtxInputAdapter
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.oneOf
import ktx.ashley.plusAssign
import ktx.math.vec3

class PlayerInputSystem(
    private val inputMultiplexer: InputMultiplexer,
    private val camera: Camera,
    private val world: World
) :
    EntitySystem(), KtxInputAdapter {

    private val playerEntity = world.player

    override fun addedToEngine(engine: Engine) {
        inputMultiplexer.addProcessor(this)
    }

    override fun removedFromEngine(engine: Engine) {
        inputMultiplexer.removeProcessor(this)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (screenX > Gdx.graphics.width / 16 * (16 - HUD_WIDTH)) { //Ignore Hud clicks
            return true
        }

        val coordinates = camera.unproject(vec3(screenX.toFloat(), screenY.toFloat()))
        val playerPosition = playerEntity[PositionComponent.mapper]!!
        val targetPosition = PositionComponent(coordinates.x.toInt(), coordinates.y.toInt(), world.storey.z)
        val direction = Direction.fromVector(targetPosition.toVec2().sub(playerPosition.toVec2()))

        if (direction === Direction.NONE) {
            doNothing()
            return true
        }

        if (playerEntity.has(InDanger.mapper)) {
            move(direction)

            return true
        }

        val pathfinder = PathFinder(world.storey, engine)
        moveInPath(pathfinder.findPath(playerPosition, targetPosition))

        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        if (playerEntity.has(ActionComponent.mapper)) return true

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
            engine.getEntityAtPosition(playerEntity[PositionComponent.mapper]!!, oneOf(GoUp::class, GoDown::class).get())

        if (entity !== null) {
            if (entity.has(GoUp.mapper)) {
                playerEntity += ActionComponent(GoUpstairs(engine, world, playerEntity))
                return
            }
            if (entity.has(GoDown.mapper)) {
                playerEntity += ActionComponent(GoDownstairs(engine, world, playerEntity))
                return
            }
        }
    }

    private fun move(direction: Direction) {
        if (playerEntity.has(Dead.mapper)) {
            doNothing()
            return
        }
        playerEntity += ActionComponent(AggressiveMove(engine, playerEntity, direction, world))
    }

    private fun moveInPath(path: Path) {
        if (playerEntity.has(Dead.mapper) || path.isEmpty()) {
            doNothing()
            return
        }
        playerEntity += ActionComponent(MoveInPath(engine, playerEntity, path, world.storey, continueToMoveInNextTurn = true))
    }

    private fun doNothing() {
        playerEntity += ActionComponent(DoNothing())
    }
}