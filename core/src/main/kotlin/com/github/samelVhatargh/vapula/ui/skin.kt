package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.github.samelVhatargh.vapula.assets.FontAsset
import com.github.samelVhatargh.vapula.assets.TextureAtlasAsset
import com.github.samelVhatargh.vapula.assets.get
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.scrollPane
import ktx.style.skin

fun createSkin(assets: AssetManager) {
    val atlas = assets[TextureAtlasAsset.UI]
    Scene2DSkin.defaultSkin = skin(atlas) {
        label {
            font = assets[FontAsset.SIZE_24]
            fontColor = Color.WHITE
        }
        scrollPane {

        }
    }
}