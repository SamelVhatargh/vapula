package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.createEmptyTiles
import ktx.ashley.mapperFor

class GameMap : Component, Pool.Poolable {
    var width = 0
    var height = 0
    var tiles = createEmptyTiles(width, height)
        set(value) {
            field = value
            shouldComputeTileGraphics = true
        }


    var shouldComputeTileGraphics = false

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
        shouldComputeTileGraphics = true
    }


    fun isWalkable(x: Int, y: Int): Boolean = tiles[x][y].terrain == Terrain.FLOOR

    fun isExplored(position: Position): Boolean = tiles[position.x][position.y].explored

    fun blockSight(x: Int, y: Int): Boolean {
        return try {
            tiles[x][y].blockSight
        } catch (e: ArrayIndexOutOfBoundsException) {
            true
        }
    }

    fun markAsExplored(position: Position) {
        tiles[position.x][position.y].explored = true
    }

    fun getNeighbor(position: Position, direction: Direction): Tile {
        return try {
            tiles[position.x + direction.x][position.y + direction.y]
        } catch (e: ArrayIndexOutOfBoundsException) {
            Tile(Position(position.x + direction.x, position.y + direction.y), Terrain.WALL)
        }
    }

    fun getNeighbor(tile: Tile, direction: Direction): Tile {
        return getNeighbor(tile.position, direction)
    }

    override fun reset() {
        tiles = createEmptyTiles(width, height)
    }

    companion object {
        val mapper = mapperFor<GameMap>()
    }
}