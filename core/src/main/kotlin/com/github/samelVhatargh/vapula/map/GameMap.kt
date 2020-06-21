package com.github.samelVhatargh.vapula.map

import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.generators.MapGenerator
import ktx.math.vec2

private enum class Neighbor {
    NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
}

class GameMap(private val width: Int, private val height: Int) {
    private var tiles = createEmptyTiles(width, height)

    val drawTiles = mutableListOf<DrawTile>()

    fun generate(generator: MapGenerator) {
        tiles = generator.getTiles(width, height)
        computeDrawTiles()
    }

    fun getRandomFloorTilePosition(): Position {
        var i = 1
        while (i < 1000) {
            val x = (0 until width).random()
            val y = (0 until height).random()

            if (tiles[x][y].terrain == Terrain.FLOOR) return Position(x, y)

            i++
        }

        return Position(5, 5)
    }

    private fun computeDrawTiles() {
        drawTiles.clear()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val tile = tiles[x][y]
                val position = vec2(x.toFloat(), y.toFloat())
                if (tile.terrain === Terrain.FLOOR) {
                    drawTiles.add(DrawTile(position, FLOOR))
                    continue
                }

                var tileNumber = 0
                if (getNeighbor(x, y, Neighbor.NORTH).terrain == Terrain.WALL) tileNumber += 1
                if (getNeighbor(x, y, Neighbor.EAST).terrain == Terrain.WALL) tileNumber += 2
                if (getNeighbor(x, y, Neighbor.SOUTH).terrain == Terrain.WALL) tileNumber += 4
                if (getNeighbor(x, y, Neighbor.WEST).terrain == Terrain.WALL) tileNumber += 8

                if (tileNumber != 15) {
                    drawTiles.add(DrawTile(position, "$WALL$tileNumber"))
                }

                if (getNeighbor(x, y, Neighbor.NORTH_WEST).terrain == Terrain.FLOOR)
                    drawTiles.add(DrawTile(position, NORTH_WEST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.NORTH_EAST).terrain == Terrain.FLOOR)
                    drawTiles.add(DrawTile(position, NORTH_EAST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.SOUTH_EAST).terrain == Terrain.FLOOR)
                    drawTiles.add(DrawTile(position, SOUTH_EAST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.SOUTH_WEST).terrain == Terrain.FLOOR)
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
            Tile(Terrain.WALL)
        }
    }

    /**
     * Менят тайл на карте.
     *
     * Используется для дебаггинга
     */
    fun switchTile(x: Int, y: Int) {
        tiles[x][y] = if (tiles[x][y].terrain == Terrain.FLOOR) Tile(Terrain.WALL) else Tile(Terrain.FLOOR)
        computeDrawTiles()
    }


    fun isWalkable(x: Int, y: Int): Boolean = tiles[x][y].terrain == Terrain.FLOOR

    fun isExplored(position: Position): Boolean = tiles[position.x][position.y].explored

    fun blockSight(x: Int, y: Int): Boolean {
        try {
            return tiles[x][y].blockSight
        } catch (e: ArrayIndexOutOfBoundsException) {
            return true
        }
    }

    fun markAsExplored(position: Position) {
        tiles[position.x][position.y].explored = true
    }
}

fun createEmptyTiles(width: Int, height: Int): Array<Array<Tile>> =
    Array(width) { Array(height) { Tile(Terrain.WALL) } }