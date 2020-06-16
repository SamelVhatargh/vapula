package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.graphics.use

class MapRenderer(atlas: TextureAtlas, private val batch: SpriteBatch) {

    private val sprites = TileSprites(atlas)

    fun renderMap(map: Map) {
        batch.use {
            map.drawTiles.forEach { tile ->
                val sprite = sprites[tile.spriteName]
                if (sprite != null) {
                    sprite.setPosition(tile.position.x, tile.position.y)
                    sprite.draw(batch)
                }
            }
        }
    }


}