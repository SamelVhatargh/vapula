package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.createEmptyTiles

private enum class Direction(val x: Int, val y: Int) {
    NORTH(0, 1),
    SOUTH(0, -1),
    WEST(1, 0),
    EAST(-1, 0)
}

class DrunkardWalkDungeon : MapGenerator {

    override fun getTiles(width: Int, height: Int): Array<Array<Tile>> {
        val tiles = createEmptyTiles(width, height)

        val tilesCount = width * height
        val percentage = 0.25f

        var currentTile = Pair((1 until width).random(), (1 until height).random())
        var floorTilesCount = 0

        tiles[currentTile.first][currentTile.second] = Tile(Terrain.FLOOR)

        var directions = listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)

        while (floorTilesCount <= tilesCount * percentage) {
            val direction = directions.random()
            directions = listOf(
                Direction.NORTH,
                Direction.SOUTH,
                Direction.EAST,
                Direction.WEST,
                direction,
                direction,
                direction
            )
            val newTile = Pair(currentTile.first + direction.x, currentTile.second + direction.y)
            if (newTile.first < 1 || newTile.first >= width - 1
                || newTile.second < 1 || newTile.second >= height - 1
            ) continue

            val oldTile = tiles[newTile.first][newTile.second]
            tiles[newTile.first][newTile.second] = Tile(Terrain.FLOOR)
            currentTile = newTile

            if (oldTile.terrain == Terrain.WALL) floorTilesCount++
        }

        return tiles
    }
}