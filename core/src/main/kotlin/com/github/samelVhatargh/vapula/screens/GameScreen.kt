package com.github.samelVhatargh.vapula.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.addModalDialogWindow
import ktx.app.KtxScreen

class GameScreen(
    private val engine: Engine,
    private val viewport: Viewport,
) : KtxScreen {

    override fun show() {
        super.show()
        engine.addModalDialogWindow(
            "Welcome!",
            "Vapula have unleashed hordes of monsters. Kill him to save the village."
        )
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
}