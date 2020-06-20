package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.createEmptyTiles

/**
 * Генерирует созданную руками карту, использовавшуюся для отладки процесса рисования карты
 */
class TestMapGenerator : MapGenerator {

    override fun getTiles(width: Int, height: Int): Array<Array<Tile>> {
        val tiles = createEmptyTiles(width, height)

        for (x in 3..7) {
            for (y in 4..6) {
                tiles[x][y] = Tile(Terrain.FLOOR)
            }
        }

        for (x in 10..13) {
            for (y in 2..5) {
                tiles[x][y] = Tile(Terrain.FLOOR)
            }
        }

        tiles[8][5] = Tile(Terrain.FLOOR)
        tiles[9][5] = Tile(Terrain.FLOOR)

        tiles[12][4] = Tile(Terrain.WALL)

        tiles[1][2] = Tile(Terrain.FLOOR)

        tiles[3][2] = Tile(Terrain.FLOOR)

        return tiles
    }
}