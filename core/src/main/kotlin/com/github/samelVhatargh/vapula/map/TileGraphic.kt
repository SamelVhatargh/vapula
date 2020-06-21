package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.math.Vector2

const val FLOOR = "Tile"
const val EMPTY = ""
const val NORTH_WEST_CORNER = "WallCorner1"
const val NORTH_EAST_CORNER = "WallCorner2"
const val SOUTH_EAST_CORNER = "WallCorner3"
const val SOUTH_WEST_CORNER = "WallCorner4"
const val WALL = "Wall"

/**
 * Хранит информацию для рисования карты
 */
data class TileGraphic(val position: Vector2, val spriteName: String, val priority: Int = 0)