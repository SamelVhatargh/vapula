package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.Move
import com.github.samelVhatargh.vapula.systems.PlayerInput
import com.github.samelVhatargh.vapula.systems.Render
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with

class Vapula : KtxGame<KtxScreen>() {

    private var viewport = FitViewport(16f, 9f)
    private val batch by lazy { SpriteBatch() }

    private val engine by lazy {
        PooledEngine().apply {
            addSystem(Move())
            addSystem(Render(batch, viewport))
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        val sprites = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

        val player = engine.entity {
            with<Position> {
                x = 5
                y = 5
            }
            with<Graphics> {
                setSpriteRegion(sprites.findRegion("character"))
            }
        }

        Gdx.input.inputProcessor = PlayerInput(player)

        addScreen(GameScreen(engine, viewport))
        setScreen<GameScreen>()
    }
}