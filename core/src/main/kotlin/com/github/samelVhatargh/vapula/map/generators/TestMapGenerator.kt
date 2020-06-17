package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Tile

/**
 * Генерирует созданную руками карту, использовавшуюся для отладки процесса рисования карты
 */
class TestMapGenerator : MapGenerator {

    override fun getTiles(width: Int, height: Int): Array<Array<Tile>> {
        val tiles = Array(width) { Array(height) { Tile.WALL } }

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

        return tiles
    }
}