package com.github.samelVhatargh.vapula.tests

import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.generators.Map

open class MapBaseTest {

    /**
     * Создает объект карты по переданным аргументам, каждый аргумент - ряд из карты
     */
    fun map(vararg mapDescription: String): Map {
        val height = mapDescription.size
        val width = mapDescription.first().length

        val tiles = Array(width) { Array(height) { Tile(Position(0, 0), Terrain.WALL) } }

        mapDescription.forEachIndexed { y, column ->
            column.forEachIndexed { x, char ->
                tiles[x][y] =
                    if (char == '#') Tile(Position(x, y), Terrain.WALL) else Tile(Position(x, y), Terrain.FLOOR)
            }
        }
        return Map(tiles, listOf(), listOf())
    }

    /**
     * Создает объект карты из текущей карты
     */
    fun map(gameMap: GameMap): Map = Map(gameMap.tiles, listOf(), listOf())
}