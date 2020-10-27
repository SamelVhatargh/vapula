package com.github.samelVhatargh.vapula.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.EventType
import com.github.samelVhatargh.vapula.getNotifier
import com.github.samelVhatargh.vapula.ui.Hud
import com.strongjoshua.console.GUIConsole
import ktx.actors.plusAssign
import ktx.app.KtxScreen
import ktx.ashley.allOf
import ktx.ashley.get

class GameScreen(private val engine: Engine, private val viewport: Viewport, private val console: GUIConsole) :
    KtxScreen {

    private val stage: Stage by lazy {
        Stage(FitViewport(16 * 64f, 9 * 64f))
    }

    private val hud = Hud()

    override fun show() {
        super.show()
        setupUI()
        engine.getNotifier().addObserver(EventType.ENTITY_DAMAGED, hud)
    }

    override fun hide() {
        engine.getNotifier().removeObserver(hud)
    }

    private fun setupUI() {
        stage += hud.panel
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
        engine.getNotifier().removeObserver(hud)
    }
}