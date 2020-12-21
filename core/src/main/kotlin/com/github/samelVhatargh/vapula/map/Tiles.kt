package com.github.samelVhatargh.vapula.map

enum class Terrain {
    FLOOR, WALL
}

data class Tile(val terrain: Terrain, var explored: Boolean = false, val blockSight: Boolean = terrain == Terrain.WALL) {
    override fun toString(): String {
        if (terrain === Terrain.WALL) {
            return "#"
        }

        return "."
    }
}

fun createEmptyTiles(width: Int, height: Int): Array<Array<Tile>> =
    Array(width) { Array(height) { Tile(Terrain.WALL) } }