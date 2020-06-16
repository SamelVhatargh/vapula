package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas

const val FLOOR = "Tile"
const val EMPTY = ""
const val CORNER_LEFT = "Corner1"
const val CORNER_RIGHT = "Corner2"
const val CORNER_BOTH = "Corner3"
const val WALL = "Wall"
const val WALL_DOWN = "WallDown"
const val WALL_LEFT = "WallRight"
const val WALL_RIGHT = "WallLeft"
const val WALL_BOTH = "WallBoth"
const val WALL_LEFT_CORNER = "WallRightCorner"
const val WALL_RIGHT_CORNER = "WallLeftCorner"
const val WALL_BOTH_CORNER = "WallBothCorner"

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