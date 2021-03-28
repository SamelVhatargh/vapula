package com.github.samelVhatargh.vapula.console.commands

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.github.samelVhatargh.vapula.map.GameMap
import ktx.app.KtxInputAdapter
import ktx.math.vec3

/**
 * Включает или отключает режим рисования карты
 */
class MapDrawingMode(
    private val inputMultiplexer: InputMultiplexer,
    private val camera: Camera,
    private val gameMap: GameMap
) :
    KtxInputAdapter {

    var enabled: Boolean = false
        set(value) {
            field = value
            if (value) {
                inputMultiplexer.addProcessor(this)
                return
            }

            inputMultiplexer.removeProcessor(this)
        }


    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val position = camera.unproject(vec3(screenX.toFloat(), screenY.toFloat()))

        if (button == Input.Buttons.LEFT) {
            gameMap.switchTile(position.x.toInt(), position.y.toInt())
        }

        return true
    }
}