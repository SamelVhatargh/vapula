package com.github.samelVhatargh.vapula.tests

import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.generators.Map

open class MapBaseTest {

    /**
     * Создает объект карты по переданным аргументам, каждый аргумент - ряд из карты
     */
    fun map(vararg mapDescription: String): Map {
        val width = mapDescription.size
        val height = mapDescription.first().length

        val tiles = Array(width) { Array(height) { Tile(Terrain.WALL) } }

        mapDescription.forEachIndexed { x, row ->
            row.forEachIndexed { y, char ->
                tiles[x][y] = if (char == '#') Tile(Terrain.WALL) else Tile(Terrain.FLOOR)
            }
        }
        return Map(tiles, listOf(), listOf())
    }
}