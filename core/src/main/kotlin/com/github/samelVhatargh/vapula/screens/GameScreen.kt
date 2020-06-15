package com.github.samelVhatargh.vapula.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(private val engine: Engine) : KtxScreen {
    private val sprites = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

    override fun show() {
        engine.entity {
            with<Position> {
                x = 10
                y = 10
            }
            with<Graphics> {
                setSpriteRegion(sprites.findRegion("character"))
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }
}