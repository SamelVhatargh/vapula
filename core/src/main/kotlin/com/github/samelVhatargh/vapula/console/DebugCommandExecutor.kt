package com.github.samelVhatargh.vapula.console

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.console.commands.MapDrawingMode
import com.github.samelVhatargh.vapula.console.commands.removeFog
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.annotation.ConsoleDoc
import ktx.ashley.get
import ktx.ashley.getSystem

class DebugCommandExecutor(
    inputMultiplexer: InputMultiplexer,
    camera: Camera,
    val map: Entity,
    val player: Entity,
    val engine: Engine
) :
    CommandExecutor() {

    private val mapDrawingMode by lazy {
        MapDrawingMode(inputMultiplexer, camera, map)
    }

    /**
     * Включает или отключает режим рисования карты
     */
    @ConsoleDoc(description = "Enables or disables map drawing mode")
    fun tyriok() {
        mapDrawingMode.enabled = !mapDrawingMode.enabled
        val enabledWord = if (mapDrawingMode.enabled) "enabled" else "disabled"
        console.log("Map drawing mode $enabledWord")
    }

    /**
     * Отображает неисследованные и невидимые игроком участки  карты
     */
    @ConsoleDoc(description = "Makes all tiles of map visible by player")
    fun removeFog() {
        removeFog(map[GameMap.mapper]!!, player, engine)
    }

    /**
     * Позволяет осмотреть карту в свободном режиме
     */
    @ConsoleDoc(description = "Enables or disables camera movement on mouse hover over edge screen")
    fun eye() {
        val cameraSystem = engine.getSystem<com.github.samelVhatargh.vapula.systems.Camera>()
        cameraSystem.moveWithMouseEnabled = !cameraSystem.moveWithMouseEnabled
        val enabledWord = if (cameraSystem.moveWithMouseEnabled) "enabled" else "disabled"
        console.log("Free camera mode $enabledWord")
    }
}