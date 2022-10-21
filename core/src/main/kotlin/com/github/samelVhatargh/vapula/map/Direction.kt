package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.math.Vector2
import kotlin.math.roundToInt

enum class Direction(val x: Int, val y: Int) {
    NORTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0),
    SOUTH(0, -1),
    NORTH_EAST(1, 1),
    NORTH_WEST(-1, 1),
    SOUTH_EAST(1, -1),
    SOUTH_WEST(-1, -1),
    NONE(0, 0);

    companion object {
        fun fromVector (vec: Vector2): Direction {
            println(vec)
            vec.setLength(1f)
            println(vec)
            val x = vec.x.roundToInt()
            val y = vec.y.roundToInt()
            return when (x) {
                1 -> when (y) {
                    1 -> NORTH_EAST
                    -1 -> SOUTH_EAST
                    else -> EAST
                }
                -1 -> when (y) {
                    1 -> NORTH_WEST
                    -1 -> SOUTH_WEST
                    else -> WEST
                }
                else -> when (y) {
                    1 -> NORTH
                    -1 -> SOUTH
                    else -> NONE
                }
            }
        }
    }
}