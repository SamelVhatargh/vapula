package com.github.samelVhatargh.vapula.console

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.annotation.ConsoleDoc

class DebugCommandExecutor(inputMultiplexer: InputMultiplexer, camera: Camera, map: Entity) : CommandExecutor() {

    private val mapDrawingMode by lazy { MapDrawingMode(inputMultiplexer, camera, map) }

    /**
     * Включает или отключает режим рисования карты
     */
    @ConsoleDoc(description = "Enables or disables map drawing mode")
    fun tyriok() {
        mapDrawingMode.enabled = !mapDrawingMode.enabled
        val enabledWord = if (mapDrawingMode.enabled) "enabled" else "disabled"
        console.log("Map drawing mode $enabledWord")

        console.resetInputProcessing()
    }

}