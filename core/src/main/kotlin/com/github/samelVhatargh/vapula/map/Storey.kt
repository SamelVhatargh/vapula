package com.github.samelVhatargh.vapula.map

import com.github.samelVhatargh.vapula.map.generators.Map

/**
 * Represents a dungeon level
 */
class Storey(map: Map, val z: Int = 0) {
    var width = map.tiles.count()
    var height = map.tiles[0].count()
    var tiles = map.tiles

    /**
     * Менят тайл на карте.
     *
     * Используется для дебаггинга
     */
    fun switchTile(x: Int, y: Int) {
        val currentTile = tiles[x][y]
        tiles[x][y] = if (currentTile.terrain == Terrain.FLOOR) Tile(
            currentTile.position,
            Terrain.WALL
        ) else Tile(currentTile.position, Terrain.FLOOR)
    }


    fun isWalkable(x: Int, y: Int): Boolean = tiles[x][y].terrain == Terrain.FLOOR

    fun isExplored(position: PositionComponent): Boolean = tiles[position.x][position.y].explored

    fun blockSight(x: Int, y: Int): Boolean {
        return try {
            tiles[x][y].blockSight
        } catch (e: ArrayIndexOutOfBoundsException) {
            true
        }
    }

    fun markAsExplored(position: PositionComponent) {
        tiles[position.x][position.y].explored = true
    }

    fun getNeighbor(position: PositionComponent, direction: Direction): Tile {
        return try {
            tiles[position.x + direction.x][position.y + direction.y]
        } catch (e: ArrayIndexOutOfBoundsException) {
            Tile(PositionComponent(position.x + direction.x, position.y + direction.y), Terrain.WALL)
        }
    }

    fun getNeighbor(tile: Tile, direction: Direction): Tile {
        return getNeighbor(tile.position, direction)
    }
}