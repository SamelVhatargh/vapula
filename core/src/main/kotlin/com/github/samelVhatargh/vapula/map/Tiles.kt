package com.github.samelVhatargh.vapula.map

import com.github.samelVhatargh.vapula.components.Position

enum class Terrain {
    FLOOR, WALL
}

data class Tile(
    val position: Position,
    val terrain: Terrain,
    var explored: Boolean = false,
    val blockSight: Boolean = terrain == Terrain.WALL
) {
    override fun toString(): String {
        if (terrain === Terrain.WALL) {
            return "#"
        }

        return "."
    }
}

fun createEmptyTiles(width: Int, height: Int): Array<Array<Tile>> =
    Array(width) { x -> Array(height) { y -> Tile(Position(x, y), Terrain.WALL) } }