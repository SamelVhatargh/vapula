package com.github.samelVhatargh.vapula.debug.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.github.samelVhatargh.vapula.map.Storey
import com.github.samelVhatargh.vapula.systems.MapRender
import ktx.app.KtxInputAdapter
import ktx.ashley.getSystem
import ktx.math.vec3

/**
 * Включает или отключает режим рисования карты
 */
class MapDrawingMode(
    private val inputMultiplexer: InputMultiplexer,
    private val camera: Camera,
    private val storey: Storey,
    private val engine: Engine
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
            storey.switchTile(position.x.toInt(), position.y.toInt())
            engine.getSystem<MapRender>().shouldComputeTileGraphics = true
        }

        return true
    }
}