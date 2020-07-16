package com.github.samelVhatargh.vapula.map

enum class Direction(val x: Int, val y: Int) {
    NORTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0),
    SOUTH(0, -1),
    NORTH_EAST(1, 1),
    NORTH_WEST(-1, 1),
    SOUTH_EAST(1, -1),
    SOUTH_WEST(-1, -1),
    NONE(0, 0)
}