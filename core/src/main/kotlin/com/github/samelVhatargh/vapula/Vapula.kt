package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.Render
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Vapula : KtxGame<KtxScreen>() {

    private val batch by lazy { SpriteBatch() }

    private val engine by lazy {
        PooledEngine().apply {
            addSystem(Render(batch))
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen(engine))
        setScreen<GameScreen>()
    }
}