package com.github.samelVhatargh.vapula.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.samelVhatargh.vapula.Vapula
import com.github.samelVhatargh.vapula.assets.FontAsset
import com.github.samelVhatargh.vapula.assets.MusicAsset
import com.github.samelVhatargh.vapula.assets.SoundAsset
import com.github.samelVhatargh.vapula.assets.TextureAtlasAsset
import com.github.samelVhatargh.vapula.ui.Hud
import com.github.samelVhatargh.vapula.ui.createSkin
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class LoadingScreen(val assets: AssetManager, private val game: Vapula, private val hud: Hud) : KtxScreen {

    private val width = Gdx.graphics.width.toFloat()
    private val height = 15f

    private val shapeRenderer = ShapeRenderer()

    override fun show() {
        SoundAsset.values().forEach { if (it !== SoundAsset.EMPTY) assets.load(it.descriptor) }
        MusicAsset.values().forEach { assets.load(it.descriptor) }
        TextureAtlasAsset.values().forEach { assets.load(it.descriptor) }
        FontAsset.values().forEach { assets.load(it.descriptor) }
    }

    override fun render(delta: Float) {
        val progress = assets.progress
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(0f, 0f, width, height)
            it.color = Color.BLACK
            it.rect(1f, 1f, width - 2f, height - 2f)
            it.color = Color.WHITE
            it.rect(2f, 2f, progress * width - 4f, height - 4f)
        }

        if (assets.update()) {
            createSkin(assets)
            hud.setupUI()

            game.setScreen<GameScreen>()
            shapeRenderer.dispose()
        }
    }

    override fun dispose() {
        shapeRenderer.disposeSafely()
        super.dispose()
    }
}