package com.github.samelVhatargh.vapula.map

import ktx.math.vec2

class GameMap(private val width: Int, private val height: Int) {
    val tiles = Array(width) { Array(height) { Tile.WALL } }

    val drawTiles = mutableListOf<DrawTile>()

    init {

        for (x in 3..7) {
            for (y in 4..6) {
                tiles[x][y] = Tile.FLOOR
            }
        }

        for (x in 10..13) {
            for (y in 2..5) {
                tiles[x][y] = Tile.FLOOR
            }
        }

        tiles[8][5] = Tile.FLOOR
        tiles[9][5] = Tile.FLOOR

        tiles[12][4] = Tile.WALL

        tiles[1][2] = Tile.FLOOR

        tiles[3][2] = Tile.FLOOR

        computeDrawTiles()
    }

    fun computeDrawTiles() {
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

                try {
                    if (tiles[x][y + 1] == Tile.WALL) tileNumber += 1
                } catch (e: ArrayIndexOutOfBoundsException) {
                    tileNumber += 1
                }
                try {
                    if (tiles[x + 1][y] == Tile.WALL) tileNumber += 2
                } catch (e: ArrayIndexOutOfBoundsException) {
                    tileNumber += 2
                }
                try {
                    if (tiles[x][y - 1] == Tile.WALL) tileNumber += 4
                } catch (e: ArrayIndexOutOfBoundsException) {
                    tileNumber += 4
                }
                try {
                    if (tiles[x - 1][y] == Tile.WALL) tileNumber += 8
                } catch (e: Exception) {
                    tileNumber += 8
                }

                if (tileNumber != 15) {
                    drawTiles.add(DrawTile(position, "Wall$tileNumber"))
                }

                for (i in -1..1 step 2) {
                    for (j in -1..1 step 2) {
                        try {
                            if (tiles[x + i][y + j] == Tile.FLOOR) {
                                val cornerNumber = when (i * 10 + j) {
                                    -10 + 1 -> 1
                                    10 + 1  -> 2
                                    10 - 1  -> 3
                                    -10 - 1  -> 4
                                    else -> 1
                                }
                                drawTiles.add(DrawTile(position, "WallCorner$cornerNumber", -1))
                            }
                        } catch (e: Exception) {
                        }
                    }
                }

            }
        }
        drawTiles.sortBy { it.priority }
    }
}
