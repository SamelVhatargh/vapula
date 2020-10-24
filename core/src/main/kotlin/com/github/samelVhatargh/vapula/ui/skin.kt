package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin

fun createSkin() {
    Scene2DSkin.defaultSkin = skin {
        label {
            font = BitmapFont(Gdx.files.internal("fonts/DejaVu24.fnt"))
            fontColor = Color.WHITE
        }
    }
}