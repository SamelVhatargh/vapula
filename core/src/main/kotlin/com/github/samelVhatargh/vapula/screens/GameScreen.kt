package com.github.samelVhatargh.vapula.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.ui.Hud
import com.strongjoshua.console.GUIConsole
import ktx.actors.plusAssign
import ktx.app.KtxScreen
import ktx.ashley.allOf
import ktx.ashley.get

class GameScreen(
    private val engine: Engine,
    private val viewport: Viewport,
    private val console: GUIConsole,
    private val inputMultiplexer: InputMultiplexer
) :
    KtxScreen {

    private val stage: Stage by lazy {
        Stage(FitViewport(16 * 64f, 9 * 64f))
    }

    private val hud by lazy { Hud() }

    override fun show() {
        super.show()
        setupUI()
        inputMultiplexer.addProcessor(0, stage)
        engine.notifier.addObserver(hud)
    }

    override fun hide() {
        engine.notifier.removeObserver(hud)
        inputMultiplexer.removeProcessor(stage)
    }

    private fun setupUI() {
        stage += hud.panel
        stage.scrollFocus = hud.messageScrollPane
        val player = engine.getEntitiesFor(allOf(Player::class, Stats::class).get()).first()
        hud.updatePlayerStats(player[Stats.mapper]!!)
    }

    override fun render(delta: Float) {
        engine.update(delta)
        stage.act()
        stage.draw()
        console.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
        engine.notifier.removeObserver(hud)
    }
}