package com.github.samelVhatargh.vapula

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.screens.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Vapula : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}