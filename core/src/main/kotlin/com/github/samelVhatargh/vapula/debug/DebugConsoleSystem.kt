package com.github.samelVhatargh.vapula.debug

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.game.World
import com.strongjoshua.console.GUIConsole

class DebugConsoleSystem(
    private val debugArguments: DebugArguments,
    private val inputMultiplexer: InputMultiplexer,
    private val viewport: Viewport,
    private val world: World,
) : EntitySystem() {

    private val console = GUIConsole()

    override fun addedToEngine(engine: Engine) {
        val commandExecutor = DebugCommandExecutor(inputMultiplexer, viewport.camera, world, engine)
        console.apply {
            setCommandExecutor(commandExecutor)
            displayKeyID = Input.Keys.GRAVE
        }

        debugArguments.getStartupCommands().forEach { command ->
            console.execCommand(command)
        }
    }

    override fun update(deltaTime: Float) {
        console.draw()
    }
}