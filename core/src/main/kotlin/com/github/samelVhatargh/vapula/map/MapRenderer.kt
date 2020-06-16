package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.graphics.use

class MapRenderer(atlas: TextureAtlas, private val batch: SpriteBatch) {

    private val sprites = TileSprites(atlas)

    fun render(map: Map) {
        batch.use {
            val tiles = map.drawTiles
            for (x in tiles.indices) {
                for (y in tiles[x].indices) {

                    val sprite = sprites[tiles[x][y]]
                    if (sprite != null) {
                        sprite.setPosition(x.toFloat(), y.toFloat())
                        sprite.draw(batch)
                    }
                }
            }
        }
    }
}