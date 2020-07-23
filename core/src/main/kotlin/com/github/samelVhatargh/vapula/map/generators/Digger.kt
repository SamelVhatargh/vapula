package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.utility.random
import ktx.log.debug
import ktx.log.logger

/**
 * Создает комнаты и тоннели
 */
class Digger(private val tiles: Array<Array<Tile>>) {

    companion object {
        val log = logger<Digger>()
    }

    fun dig(start: MapObject, end: MapObject): Tunnel {
        return dig(start.getRandomPosition(), end.getRandomPosition())
    }

    private fun dig(start: Position, end: Position): Tunnel {
        log.debug { "tunnel - $start; $end" }

        val xRange = if (start.x < end.x) start.x..end.x else start.x downTo end.x
        val yRange = if (start.y < end.y) start.y..end.y else start.y downTo end.y

        val tunnelTiles = mutableListOf<Position>()

        if (random.range(1..2) == 1) {
            for (x in xRange) {
                tunnelTiles.add(Position(x, start.y))
                tiles[x][start.y] = Tile(Terrain.FLOOR)
            }
            for (y in yRange) {
                tunnelTiles.add(Position(end.x, y))
                tiles[end.x][y] = Tile(Terrain.FLOOR)
            }
        } else {
            for (y in yRange) {
                tunnelTiles.add(Position(start.x, y))
                tiles[start.x][y] = Tile(Terrain.FLOOR)
            }
            for (x in xRange) {
                tunnelTiles.add(Position(x, end.y))
                tiles[x][end.y] = Tile(Terrain.FLOOR)
            }
        }

        return Tunnel(start, end, tunnelTiles)
    }

    fun dig(room: Room) {
        for (x in room.position.x until (room.position.x + room.width)) {
            for (y in room.position.y until (room.position.y + room.height)) {
                tiles[x][y] = Tile(Terrain.FLOOR)
            }
        }
    }
}