package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.Render
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Vapula : KtxGame<KtxScreen>() {

    private var viewport = FitViewport(16f, 9f)
    private val batch by lazy { SpriteBatch() }

    private val engine by lazy {
        PooledEngine().apply {
            addSystem(Render(batch, viewport))
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen(engine, viewport))
        setScreen<GameScreen>()
    }
}