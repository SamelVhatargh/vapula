package com.github.samelVhatargh.vapula.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.app.KtxScreen
import ktx.graphics.use

class GameScreen : KtxScreen {

    private val batch = SpriteBatch()

    private val sprites = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

    private val player = Sprite(sprites.findRegion("character"))

    override fun render(delta: Float) {
        batch.use {
            player.draw(batch)
        }
    }
}