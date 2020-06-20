package com.github.samelVhatargh.vapula.map

enum class Terrain {
    FLOOR, WALL
}

data class Tile(val terrain: Terrain)