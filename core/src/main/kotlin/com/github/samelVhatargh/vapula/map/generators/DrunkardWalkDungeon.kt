package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.createEmptyTiles
import kotlin.math.min

private enum class Direction(val x: Int, val y: Int) {
    NORTH(0, 1),
    SOUTH(0, -1),
    WEST(1, 0),
    EAST(-1, 0)
}

class DrunkardWalkDungeon(private val percentage: Float = 0.25f) : MapGenerator {

    override fun getTiles(width: Int, height: Int): Array<Array<Tile>> {
        val tiles = createEmptyTiles(width, height)

        val tilesCount = width * height
        val edgesCount = 2 * width + 2 * (height - 2)
        val maxFloorCount = min((tilesCount * percentage).toInt(), tilesCount - edgesCount)

        var currentTile = Pair((1 until (width - 1)).random(), (1 until (height - 1)).random())
        tiles[currentTile.first][currentTile.second] = Tile(Terrain.FLOOR)
        var floorTilesCount = 1

        var directions = listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)

        while (floorTilesCount < maxFloorCount) {
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
            ) {
                directions = mutableListOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
                directions.remove(direction)
                continue
            }

            val oldTile = tiles[newTile.first][newTile.second]
            tiles[newTile.first][newTile.second] = Tile(Terrain.FLOOR)
            currentTile = newTile

            if (oldTile.terrain == Terrain.WALL) floorTilesCount++
        }

        return tiles
    }
}