package com.github.samelVhatargh.vapula.map

private const val TOP_WALL_BIT = 0b000000010
private const val LEFT_WALL_BIT = 0b000100000
private const val RIGHT_WALL_BIT = 0b000001000
private const val BOTTOM_WALL_BIT = 0b010000000

private const val SINGLE_WALL_BIT = 0b111101111

private const val RIGHT_TOP_CORNER_BIT = 0b001000000
private const val RIGHT_BOTTOM_CORNER_BIT = 0b000000001
private const val LEFT_TOP_CORNER_BIT = 0b100000000
private const val LEFT_BOTTOM_CORNER_BIT = 0b000000100

class Map(private val width: Int, private val height: Int) {
    val tiles = Array(width) { Array(height) { Tile.WALL } }

    val drawTiles = Array(width) { Array(height) { EMPTY } }

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
        for (x in 0 until width) {
            for (y in 0 until height) {
                drawTiles[x][y] = EMPTY
            }
        }

        for (x in 0 until width) {
            for (y in 0 until height) {
                val tile = tiles[x][y]
                if (tile === Tile.FLOOR) {
                    drawTiles[x][y] = FLOOR
                    continue
                }

                val bits = getNeighboursBitSet(x, y)

                if (bits and TOP_WALL_BIT == TOP_WALL_BIT) {
                    drawTiles[x][y] = WALL
                }
                if (bits and LEFT_WALL_BIT == LEFT_WALL_BIT) {
                    drawTiles[x][y] = WALL_LEFT
                }
                if (bits and RIGHT_WALL_BIT == RIGHT_WALL_BIT) {
                    drawTiles[x][y] = WALL_RIGHT
                }
                if (bits and BOTTOM_WALL_BIT == BOTTOM_WALL_BIT) {
                    drawTiles[x][y] = WALL_DOWN
                }


                if (bits == RIGHT_TOP_CORNER_BIT) {
                    drawTiles[x][y] = CORNER_RIGHT
                }
                if (bits == LEFT_TOP_CORNER_BIT) {
                    drawTiles[x][y] = CORNER_LEFT
                }
                if (bits == RIGHT_BOTTOM_CORNER_BIT) {
                    drawTiles[x][y] = WALL_RIGHT_CORNER
                }
                if (bits == LEFT_BOTTOM_CORNER_BIT) {
                    drawTiles[x][y] = WALL_LEFT_CORNER
                }
            }
        }
    }

    private fun getNeighboursBitSet(x: Int, y: Int): Int {
        var result = 0b000000000
        var currentBit = 0b000000001

        for (j in -1..1) {
            for (i in 1 downTo -1) {
                try {
                    if (tiles[x + i][y + j] == Tile.FLOOR) result += currentBit
                } catch (e: ArrayIndexOutOfBoundsException) {
                }
                currentBit = currentBit shl 1
            }
        }

        return result
    }
}
