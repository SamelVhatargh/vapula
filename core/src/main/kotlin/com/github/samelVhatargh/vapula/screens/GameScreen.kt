package com.github.samelVhatargh.vapula.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(private val engine: Engine, private val viewport: Viewport) : KtxScreen {
    private val sprites = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

    override fun show() {
        engine.entity {
            with<Position> {
                x = 5
                y = 5
            }
            with<Graphics> {
                setSpriteRegion(sprites.findRegion("character"))
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
}