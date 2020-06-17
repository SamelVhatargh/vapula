package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas

const val FLOOR = "Tile"
const val EMPTY = ""
const val NORTH_WEST_CORNER = "WallCorner1"
const val NORTH_EAST_CORNER = "WallCorner2"
const val SOUTH_EAST_CORNER = "WallCorner3"
const val SOUTH_WEST_CORNER = "WallCorner4"
const val WALL = "Wall"

class TileSprites(private val atlas: TextureAtlas) {

    private val sprites = mutableMapOf<String, Sprite>()

    operator fun get(name: String): Sprite? {
        if (name == EMPTY) {
            return null
        }

        val existingSprite = sprites[name]
        if (existingSprite != null) return existingSprite

        val region = atlas.findRegion(name)
        require(region != null) { "Cant load sprite $name" }

        val loadedSprite = Sprite(region).apply {
            setSize(1f, 1f)
        }
        sprites[name] = loadedSprite
        return loadedSprite
    }
}