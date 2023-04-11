package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.github.samelVhatargh.vapula.assets.FontAsset
import com.github.samelVhatargh.vapula.assets.TextureAtlasAsset
import com.github.samelVhatargh.vapula.assets.get
import ktx.scene2d.Scene2DSkin
import ktx.style.*
import ktx.style.scrollPane

enum class LabelStyle {
    CAPTION, BASE, SMALL
}

fun createSkin(assets: AssetManager) {
    val atlas = assets[TextureAtlasAsset.UI]
    Scene2DSkin.defaultSkin = skin(atlas) {
        label {
            font = assets[FontAsset.SIZE_20]
            fontColor = Color.WHITE
        }
        label (name = LabelStyle.CAPTION.name){
            font = assets[FontAsset.SIZE_24]
        }
        label (name = LabelStyle.BASE.name){
            font = assets[FontAsset.SIZE_20]
        }
        label (name = LabelStyle.SMALL.name){
            font = assets[FontAsset.SIZE_15]
        }
        button {
            up = it["button"]
            over = it["buttonOver"]
            pressedOffsetX = 1f
            pressedOffsetY = -1f
        }
        scrollPane {

        }
    }
}