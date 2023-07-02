package com.github.samelVhatargh.vapula.map

enum class Terrain {
    FLOOR, WALL
}

data class Tile(
    val position: PositionComponent,
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
    Array(width) { x -> Array(height) { y -> Tile(PositionComponent(x, y), Terrain.WALL) } }