package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.scrollPane
import ktx.style.skin

fun createSkin() {
    val atlas = TextureAtlas(Gdx.files.internal("graphics/ui.atlas"))
    Scene2DSkin.defaultSkin = skin(atlas) {
        label {
            font = BitmapFont(Gdx.files.internal("fonts/DejaVu24.fnt"), atlas.findRegion("DejaVu24"))
            fontColor = Color.WHITE
        }
        scrollPane {

        }
    }
}