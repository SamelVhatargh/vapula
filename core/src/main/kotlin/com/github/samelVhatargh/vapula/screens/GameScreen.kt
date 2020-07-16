package com.github.samelVhatargh.vapula.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.viewport.Viewport
import com.strongjoshua.console.GUIConsole
import ktx.app.KtxScreen

class GameScreen(private val engine: Engine, private val viewport: Viewport, private val console: GUIConsole) :
    KtxScreen {

    override fun render(delta: Float) {
        engine.update(delta)
        console.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
}