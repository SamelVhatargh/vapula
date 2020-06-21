package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.toPosition
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import ktx.ashley.get
import ktx.graphics.use
import ktx.math.vec2


private const val FLOOR = "Tile"
private const val EMPTY = ""
private const val NORTH_WEST_CORNER = "WallCorner1"
private const val NORTH_EAST_CORNER = "WallCorner2"
private const val SOUTH_EAST_CORNER = "WallCorner3"
private const val SOUTH_WEST_CORNER = "WallCorner4"
private const val WALL = "Wall"

private enum class Neighbor {
    NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
}

private data class TileGraphic(val position: Vector2, val spriteName: String, val priority: Int = 0)

class MapRender(
    private val atlas: TextureAtlas,
    private val batch: SpriteBatch,
    private val player: Entity,
    map: Entity
) : EntitySystem() {

    private val spriteCache = mutableMapOf<String, Sprite>()

    private val fogOfWarSprite = Sprite(atlas.findRegion("white")).apply {
        setColor(0f, 0f, 0f, 0.75f)
    }

    private val gameMap = map[GameMap.mapper]!!
    private val tileGraphics = mutableListOf<TileGraphic>()


    override fun update(deltaTime: Float) {
        computeTileGraphics()
        renderMap()
    }

    private fun renderMap() {
        val fogOfWar = mutableSetOf<Vector2>()
        val fov = player[FieldOfView.mapper]!!

        batch.use {
            tileGraphics.forEach { tile ->
                val sprite = getSprite(tile.spriteName)
                if (sprite != null) {
                    if (!fov.isVisible(tile.position)) {
                        if (!fogOfWar.contains(tile.position)) fogOfWar.add(tile.position)
                    } else {
                        gameMap.markAsExplored(tile.position.toPosition())
                    }

                    if (gameMap.isExplored(tile.position.toPosition())) {
                        sprite.setPosition(tile.position.x, tile.position.y)
                        sprite.draw(batch)
                    }
                }
            }

            fogOfWar.forEach { position ->
                fogOfWarSprite.setPosition(position.x, position.y)
                fogOfWarSprite.draw(batch)
            }
        }
    }

    private fun getSprite(name: String): Sprite? {
        if (name == EMPTY) {
            return null
        }

        var sprite = spriteCache[name]
        if (sprite == null) {
            val region = atlas.findRegion(name)
            require(region != null) { "Cant load sprite $name" }

            sprite = Sprite(region).apply {
                setSize(1f, 1f)
            }
            spriteCache[name] = sprite
        }
        return sprite
    }

    private fun computeTileGraphics() {
        if (!gameMap.shouldComputeTileGraphics) return

        gameMap.shouldComputeTileGraphics = false
        tileGraphics.clear()

        for (x in 0 until gameMap.width) {
            for (y in 0 until gameMap.height) {
                val tile = gameMap.tiles[x][y]
                val position = vec2(x.toFloat(), y.toFloat())
                if (tile.terrain === Terrain.FLOOR) {
                    tileGraphics.add(TileGraphic(position, FLOOR))
                    continue
                }

                var tileNumber = 0
                if (getNeighbor(x, y, Neighbor.NORTH).terrain == Terrain.WALL) tileNumber += 1
                if (getNeighbor(x, y, Neighbor.EAST).terrain == Terrain.WALL) tileNumber += 2
                if (getNeighbor(x, y, Neighbor.SOUTH).terrain == Terrain.WALL) tileNumber += 4
                if (getNeighbor(x, y, Neighbor.WEST).terrain == Terrain.WALL) tileNumber += 8

                if (tileNumber != 15) tileGraphics.add(TileGraphic(position, "$WALL$tileNumber"))

                if (getNeighbor(x, y, Neighbor.NORTH_WEST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(position, NORTH_WEST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.NORTH_EAST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(position, NORTH_EAST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.SOUTH_EAST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(position, SOUTH_EAST_CORNER, -1))
                if (getNeighbor(x, y, Neighbor.SOUTH_WEST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(position, SOUTH_WEST_CORNER, -1))
            }
        }
        tileGraphics.sortBy { it.priority }
    }

    private fun getNeighbor(x: Int, y: Int, side: Neighbor): Tile {
        val dx = when (side) {
            Neighbor.NORTH, Neighbor.SOUTH -> 0
            Neighbor.EAST, Neighbor.NORTH_EAST, Neighbor.SOUTH_EAST -> 1
            Neighbor.WEST, Neighbor.NORTH_WEST, Neighbor.SOUTH_WEST -> -1
        }
        val dy = when (side) {
            Neighbor.EAST, Neighbor.WEST -> 0
            Neighbor.NORTH, Neighbor.NORTH_EAST, Neighbor.NORTH_WEST -> 1
            Neighbor.SOUTH, Neighbor.SOUTH_EAST, Neighbor.SOUTH_WEST -> -1
        }

        return try {
            gameMap.tiles[x + dx][y + dy]
        } catch (e: ArrayIndexOutOfBoundsException) {
            Tile(Terrain.WALL)
        }
    }
}

