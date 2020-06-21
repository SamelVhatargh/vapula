package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.toPosition
import ktx.graphics.use

class MapRenderer(private val atlas: TextureAtlas, private val batch: SpriteBatch) {

    private val spriteCache = mutableMapOf<String, Sprite>()

    private val fogOfWar = Sprite(atlas.findRegion("white")).apply {
        setColor(0f, 0f, 0f, 0.75f)
    }

    fun renderMap(map: GameMap, fov: FieldOfView) {
        batch.use {
            map.tileGraphics.forEach { tile ->
                val sprite = getSprite(tile.spriteName)
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

    private fun getSprite(name: String): Sprite? {
        if (name == EMPTY) {
            return null
        }

        var sprite = spriteCache[name]
        if (sprite == null) {
            val region = atlas.findRegion(name)
            require(region != null) { "Cant load sprite $name" }

            sprite = Sprite(region).apply {
                setSize(1f, 1f)
            }
            spriteCache[name] = sprite
        }
        return sprite
    }
}