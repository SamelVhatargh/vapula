package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.createEmptyTiles

/**
 * Генерирует созданную руками карту, использовавшуюся для отладки процесса рисования карты
 */
@Suppress("unused")
class TestMapGenerator : MapGenerator {

    override fun generate(width: Int, height: Int): Map {
        val tiles = createEmptyTiles(width, height)

        val firstRoomXRange = 3..7
        val firstRoomYRange = 4..6
        for (x in firstRoomXRange) {
            for (y in firstRoomYRange) {
                tiles[x][y] = Tile(Position(x, y), Terrain.FLOOR)
            }
        }
        val secondRoomXRange = 10..13
        val secondRoomYRange = 2..5
        for (x in secondRoomXRange) {
            for (y in secondRoomYRange) {
                tiles[x][y] = Tile(Position(x, y), Terrain.FLOOR)
            }
        }

        val start = Position(8, 5)
        val end = Position(9, 5)
        val tunnel = Tunnel(start, end, listOf(start, end))

        tiles[start.x][start.y] = Tile(start, Terrain.FLOOR)
        tiles[end.x][end.y] = Tile(end, Terrain.FLOOR)

        tiles[12][4] = Tile(Position(12, 4), Terrain.WALL)

        tiles[1][2] = Tile(Position(1, 2), Terrain.FLOOR)

        tiles[3][2] = Tile(Position(3, 2), Terrain.FLOOR)

        return Map(
            tiles,
            listOf(
                Room(
                    Position(firstRoomXRange.first - 1, firstRoomYRange.first - 1),
                    firstRoomXRange.count() + 2,
                    firstRoomYRange.count() + 2
                ),
                Room(
                    Position(secondRoomXRange.first - 1, secondRoomYRange.first - 1),
                    secondRoomXRange.count() + 2,
                    secondRoomYRange.count() + 2
                )
            ),
            listOf(tunnel)
        )
    }
}