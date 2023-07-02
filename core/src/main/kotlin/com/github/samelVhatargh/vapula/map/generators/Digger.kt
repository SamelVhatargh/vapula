package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.PositionComponent
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

    private fun dig(start: PositionComponent, end: PositionComponent): Tunnel {
        log.debug { "tunnel - $start; $end" }

        val xRange = if (start.x < end.x) start.x..end.x else start.x downTo end.x
        val yRange = if (start.y < end.y) start.y..end.y else start.y downTo end.y

        val tunnelTiles = mutableListOf<PositionComponent>()

        if (random.range(1..2) == 1) {
            for (x in xRange) {
                addTile(PositionComponent(x, start.y), tunnelTiles)
            }
            for (y in yRange) {
                addTile(PositionComponent(end.x, y), tunnelTiles)
            }
        } else {
            for (y in yRange) {
                addTile(PositionComponent(start.x, y), tunnelTiles)
            }
            for (x in xRange) {
                addTile(PositionComponent(x, end.y), tunnelTiles)
            }
        }

        return Tunnel(start, end, tunnelTiles)
    }

    private fun addTile(position: PositionComponent, tunnelTiles: MutableList<PositionComponent>) {
        tunnelTiles.add(position)
        tiles[position.x][position.y] = Tile(position, Terrain.FLOOR)
    }

    fun dig(room: Room) {
        val xRange = room.position.x until (room.position.x + room.width)
        val yRange = room.position.y until (room.position.y + room.height)
        for (x in xRange) {
            for (y in yRange) {
                val isCorner = (x == xRange.first && (y == yRange.first || y == yRange.last))
                        || (x == xRange.last && (y == yRange.first || y == yRange.last))

                if (isCorner && random.range(0..100) <= 35) {
                    continue
                }

                tiles[x][y] = Tile(PositionComponent(x, y), Terrain.FLOOR)
            }
        }
    }
}