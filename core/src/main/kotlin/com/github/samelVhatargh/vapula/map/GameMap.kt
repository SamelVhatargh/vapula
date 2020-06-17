package com.github.samelVhatargh.vapula.map

import com.github.samelVhatargh.vapula.map.generators.MapGenerator
import ktx.math.vec2

private enum class Neighbor {
    NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
}

class GameMap(private val width: Int, private val height: Int) {
    private var tiles = Array(width) { Array(height) { Tile.WALL } }

    val drawTiles = mutableListOf<DrawTile>()

    fun generate(generator: MapGenerator) {
        tiles = generator.getTiles(width, height)
        computeDrawTiles()
    }

    private fun computeDrawTiles() {
        drawTiles.clear()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val tile = tiles[x][y]
                val position = vec2(x.toFloat(), y.toFloat())
                if (tile === Tile.FLOOR) {
                    drawTiles.add(DrawTile(position, FLOOR))
                    continue
                }

                var tileNumber = 0
                if (getNeighbor(x, y, Neighbor.NORTH) == Tile.WALL) tileNumber += 1
                if (getNeighbor(x, y, Neighbor.EAST) == Tile.WALL) tileNumber += 2
                if (getNeighbor(x, y, Neighbor.SOUTH) == Tile.WALL) tileNumber += 4
                if (getNeighbor(x, y, Neighbor.WEST) == Tile.WALL) tileNumber += 8

                if (tileNumber != 15) {
                    drawTiles.add(DrawTile(position, "$WALL$tileNumber"))
                }

                if (getNeighbor(x, y, Neighbor.NORTH_WEST) == Tile.FLOOR)
                    drawTiles.add(DrawTile(position, NORTH_WEST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.NORTH_EAST) == Tile.FLOOR)
                    drawTiles.add(DrawTile(position, NORTH_EAST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.SOUTH_EAST) == Tile.FLOOR)
                    drawTiles.add(DrawTile(position, SOUTH_EAST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.SOUTH_WEST) == Tile.FLOOR)
                    drawTiles.add(DrawTile(position, SOUTH_WEST_CORNER, -1))
            }
        }
        drawTiles.sortBy { it.priority }
    }

    private fun getNeighbor(x: Int, y: Int, side: Neighbor): Tile {
        val dx = when (side) {
            Neighbor.NORTH, Neighbor.SOUTH -> 0
            Neighbor.EAST, Neighbor.NORTH_EAST, Neighbor.SOUTH_EAST -> 1
            Neighbor.WEST, Neighbor.NORTH_WEST, Neighbor.SOUTH_WEST -> -1
        }
        val dy = when (side) {
            Neighbor.EAST, Neighbor.WEST -> 0
            Neighbor.NORTH, Neighbor.NORTH_EAST, Neighbor.NORTH_WEST -> 1
            Neighbor.SOUTH, Neighbor.SOUTH_EAST, Neighbor.SOUTH_WEST -> -1
        }

        return try {
            tiles[x + dx][y + dy]
        } catch (e: ArrayIndexOutOfBoundsException) {
            Tile.WALL
        }
    }

    /**
     * Менят тайл на карте.
     *
     * Используется для дебаггинга
     */
    fun switchTile(x: Int, y: Int) {
        tiles[x][y] = if (tiles[x][y] == Tile.FLOOR) Tile.WALL else Tile.FLOOR
        computeDrawTiles()
    }


    fun isWalkable(newX: Int, newY: Int): Boolean = tiles[newX][newY] == Tile.FLOOR
}
