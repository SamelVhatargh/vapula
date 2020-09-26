package com.github.samelVhatargh.vapula.console

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Invulnerability
import com.github.samelVhatargh.vapula.console.commands.MapDrawingMode
import com.github.samelVhatargh.vapula.console.commands.removeFog
import com.github.samelVhatargh.vapula.systems.ShowMapCoordinates
import com.github.samelVhatargh.vapula.utility.random
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.annotation.ConsoleDoc
import ktx.ashley.*

class DebugCommandExecutor(
    private val inputMultiplexer: InputMultiplexer,
    private val camera: Camera,
    private val map: Entity,
    private val player: Entity,
    private val engine: Engine
) : CommandExecutor() {

    private val mapDrawingMode by lazy {
        MapDrawingMode(inputMultiplexer, camera, map)
    }

    val validStartupCommands = arrayOf("tyriok", "removeFog", "eye", "xy", "hemonugi")

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

    /**
     * Включает или отключает режим отображение координат карты по наведению мыши
     */
    @ConsoleDoc(description = "Enables or disables map coordinate display")
    fun xy() {
        val enabledWord = try {
            val system = engine.getSystem<ShowMapCoordinates>()
            engine.removeSystem(system)
            "disabled"
        } catch (e: MissingEntitySystemException) {
            engine.addSystem(ShowMapCoordinates(camera, inputMultiplexer))
            "enabled"
        }

        console.log("Showing map coordinates $enabledWord")
    }

    /**
     * Включает или отключает режим неуязвимости
     */
    @ConsoleDoc(description = "Enables or disables invulnerability")
    fun hemonugi() {
        val enabledWord = if (player.has(Invulnerability.mapper)) {
            player.remove<Invulnerability>()
            "lost invulnerability"
        } else {
            player.addComponent<Invulnerability>(engine)
            "becomes invulnerable"
        }

        console.log("player $enabledWord")
    }

    /**
     * Устанавливает семя для генератора случайных чисел
     */
    @ConsoleDoc(description = "Set set for random number generator", paramDescriptions = ["seed value"])
    fun seed(seed: Int) {
        random.setSeed(seed)
    }
}