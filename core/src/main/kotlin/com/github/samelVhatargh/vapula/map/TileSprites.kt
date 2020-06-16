package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas

const val FLOOR = "Tile"
const val EMPTY = ""

class TileSprites(private val atlas: TextureAtlas) {

    private val sprites = mutableMapOf<String, Sprite>()

    operator fun get(name: String): Sprite? {
        if (name == EMPTY) {
            return null
        }

        val sprite = sprites[name]
        if (sprite != null) return sprite


        val sprite1 = Sprite(atlas.findRegion(name)).apply {
            setSize(1f, 1f)
        }
        sprites[name] = sprite1
        return sprite1
    }
}