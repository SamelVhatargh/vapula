package com.github.samelVhatargh.vapula.tests

import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.generators.Map

/**
 * Test map variant which helps to get information about tile custom description
 */
class DescribedMap(val map: Map, private val description: Array<String>) {

    /**
     * Returns [Position] of first occurrence of [symbol] in [map]
     */
    fun getPosition(symbol: Char): Position? {

        description.forEachIndexed { y, column ->
            column.forEachIndexed { x, char ->
                if (char == symbol) {
                    return Position(x, y)
                }
            }
        }

        return null
    }
}

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

    fun describedMap(vararg mapDescription: String): DescribedMap {
        return DescribedMap(map(*mapDescription), arrayOf(*mapDescription))
    }

    /**
     * Creates path from string map description
     */
    protected fun path(vararg pathDescription: String): List<Position> {
        val path = mutableMapOf<Int, Position>()
        pathDescription.forEachIndexed { y, column ->
            column.forEachIndexed { x, char ->
                val index = char.toString().toIntOrNull()
                if (index !== null) {
                    path[index] = Position(x, y)
                }
            }
        }

        return path.toSortedMap().values.toList()
    }
}