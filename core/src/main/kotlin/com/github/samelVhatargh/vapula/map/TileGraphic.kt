package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.math.Vector2

/**
 * Хранит информацию для рисования карты
 */
data class TileGraphic(val position: Vector2, val spriteName: String, val priority: Int = 0)