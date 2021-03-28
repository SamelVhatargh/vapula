package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.components.VisibleIfExploredAndOutOfFieldOfView
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.GameMap
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.setPosition
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use


private const val FLOOR = "Tile"
private const val EMPTY = ""
private const val NORTH_WEST_CORNER = "WallCorner1"
private const val NORTH_EAST_CORNER = "WallCorner2"
private const val SOUTH_EAST_CORNER = "WallCorner3"
private const val SOUTH_WEST_CORNER = "WallCorner4"
private const val WALL = "Wall"

private data class TileGraphic(val position: Position, val spriteName: String, val priority: Int = 0)

class MapRender(
    private val atlas: TextureAtlas,
    private val batch: SpriteBatch,
    private val player: Entity,
    private val gameMap: GameMap
) : EntitySystem() {

    private val spriteCache = mutableMapOf<String, Sprite>()

    private val fogOfWarSprite = Sprite(atlas.findRegion("white")).apply {
        setColor(0f, 0f, 0f, 0.75f)
    }

    private val tileGraphics = mutableListOf<TileGraphic>()

    private lateinit var terrainObjects: ImmutableArray<Entity>

    var shouldComputeTileGraphics = true

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        terrainObjects = engine.getEntitiesFor(
            allOf(
                Graphics::class,
                Position::class,
                VisibleIfExploredAndOutOfFieldOfView::class
            ).get()
        )
    }


    override fun update(deltaTime: Float) {
        computeTileGraphics()
        renderMap()
    }

    private fun renderMap() {
        val fogOfWar = mutableSetOf<Position>()
        val fov = player[FieldOfView.mapper]!!

        batch.use {
            tileGraphics.forEach { tile ->
                val sprite = getSprite(tile.spriteName)
                if (sprite != null) {
                    if (!fov.isVisible(tile.position)) {
                        if (!fogOfWar.contains(tile.position)) fogOfWar.add(tile.position)
                    } else {
                        gameMap.markAsExplored(tile.position)
                    }

                    if (gameMap.isExplored(tile.position)) {
                        sprite.setPosition(tile.position)
                        sprite.draw(batch)
                    }
                }
            }

            terrainObjects.forEach { entity ->
                val position = entity[Position.mapper]!!
                if (gameMap.isExplored(position) && !fov.isVisible(position)) {
                    val graphics = entity[Graphics.mapper]!!

                    graphics.sprite.setPosition(position.x.toFloat(), position.y.toFloat())
                    graphics.sprite.draw(batch)
                }
            }

            fogOfWar.forEach { position ->
                fogOfWarSprite.setPosition(position)
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
        if (!shouldComputeTileGraphics) return

        shouldComputeTileGraphics = false
        tileGraphics.clear()

        for (x in 0 until gameMap.width) {
            for (y in 0 until gameMap.height) {
                val tile = gameMap.tiles[x][y]
                if (tile.terrain === Terrain.FLOOR) {
                    tileGraphics.add(TileGraphic(tile.position, FLOOR))
                    continue
                }

                var tileNumber = 0
                if (gameMap.getNeighbor(tile, Direction.NORTH).terrain == Terrain.WALL) tileNumber += 1
                if (gameMap.getNeighbor(tile, Direction.EAST).terrain == Terrain.WALL) tileNumber += 2
                if (gameMap.getNeighbor(tile, Direction.SOUTH).terrain == Terrain.WALL) tileNumber += 4
                if (gameMap.getNeighbor(tile, Direction.WEST).terrain == Terrain.WALL) tileNumber += 8

                if (tileNumber != 15) tileGraphics.add(TileGraphic(tile.position, "$WALL$tileNumber"))

                if (gameMap.getNeighbor(tile, Direction.NORTH_WEST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, NORTH_WEST_CORNER, -1))
                if (gameMap.getNeighbor(tile, Direction.NORTH_EAST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, NORTH_EAST_CORNER, -1))
                if (gameMap.getNeighbor(tile, Direction.SOUTH_EAST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, SOUTH_EAST_CORNER, -1))
                if (gameMap.getNeighbor(tile, Direction.SOUTH_WEST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, SOUTH_WEST_CORNER, -1))
            }
        }
        tileGraphics.sortBy { it.priority }
    }
}

