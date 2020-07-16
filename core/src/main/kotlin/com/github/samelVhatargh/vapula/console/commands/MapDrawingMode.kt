package com.github.samelVhatargh.vapula.console.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.github.samelVhatargh.vapula.components.GameMap
import ktx.app.KtxInputAdapter
import ktx.ashley.get
import ktx.math.vec3

/**
 * Включает или отключает режим рисования карты
 */
class MapDrawingMode(private val inputMultiplexer: InputMultiplexer, private val camera: Camera, map: Entity) :
    KtxInputAdapter {

    private val gameMap = map[GameMap.mapper]!!

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