package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.toPosition
import ktx.graphics.use

class MapRenderer(atlas: TextureAtlas, private val batch: SpriteBatch) {

    private val sprites = TileSprites(atlas)

    private val fogOfWar = Sprite(atlas.findRegion("white")).apply {
        setColor(0f, 0f, 0f, 0.75f)
    }

    fun renderMap(map: GameMap, fov: FieldOfView) {
        batch.use {
            map.drawTiles.forEach { tile ->
                val sprite = sprites[tile.spriteName]
                if (sprite != null) {
                    if (map.isExplored(tile.position.toPosition())) {
                        sprite.setPosition(tile.position.x, tile.position.y)
                        sprite.draw(batch)
                    }

                    //check fov
                    if (!fov.isVisible(tile.position)) {
                        fogOfWar.setPosition(tile.position.x, tile.position.y)
                        fogOfWar.draw(batch)
                    } else {
                        map.markAsExplored(tile.position.toPosition())
                    }
                }
            }
        }
    }


}