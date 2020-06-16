package com.github.samelVhatargh.vapula.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.map.MapRenderer
import com.github.samelVhatargh.vapula.map.Map
import ktx.app.KtxScreen

class GameScreen(
    private val engine: Engine,
    private val viewport: Viewport,
    sprites: TextureAtlas,
    batch: SpriteBatch
) : KtxScreen {

    private val mapRenderer = MapRenderer(sprites, batch)

    val map = Map(16 * 2, 9 * 2)


    override fun render(delta: Float) {
        mapRenderer.render(map)

        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
}