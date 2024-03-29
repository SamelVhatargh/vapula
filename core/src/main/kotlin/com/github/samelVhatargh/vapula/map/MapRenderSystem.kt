package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.samelVhatargh.vapula.game.World
import com.github.samelVhatargh.vapula.graphics.GraphicsComponent
import com.github.samelVhatargh.vapula.graphics.SpriteCache
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

private data class TileGraphic(val position: PositionComponent, val spriteName: String, val priority: Int = 0)

class MapRenderSystem(
    private val spriteCache: SpriteCache,
    private val batch: SpriteBatch,
    viewport: Viewport,
    private val world: World
) :
    EntitySystem() {

    private val player = world.player
    private val camera = viewport.camera

    private val fogOfWarSprite by lazy {
        spriteCache.getSprite("white").apply {
            setColor(0f, 0f, 0f, 0.75f)
        }
    }

    private val tileGraphics = mutableListOf<TileGraphic>()

    private lateinit var terrainObjects: ImmutableArray<Entity>

    var shouldComputeTileGraphics = true

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        terrainObjects = engine.getEntitiesFor(
            allOf(
                GraphicsComponent::class,
                PositionComponent::class,
                VisibleIfExploredAndOutOfFieldOfViewComponent::class
            ).get()
        )
    }


    override fun update(deltaTime: Float) {
        computeTileGraphics()
        renderMap()
    }

    private fun renderMap() {
        val fogOfWar = mutableSetOf<PositionComponent>()
        val fov = player[FieldOfViewComponent.mapper]!!

        batch.use(camera.combined) {
            tileGraphics.forEach { tile ->
                if (tile.spriteName != EMPTY) {
                    val sprite = spriteCache.getSprite(tile.spriteName)
                    if (!fov.isVisible(tile.position)) {
                        if (!fogOfWar.contains(tile.position)) fogOfWar.add(tile.position)
                    } else {
                        world.storey.markAsExplored(tile.position)
                    }

                    if (world.storey.isExplored(tile.position)) {
                        sprite.setPosition(tile.position)
                        sprite.draw(batch)
                    }
                }
            }

            terrainObjects.forEach { entity ->
                val position = entity[PositionComponent.mapper]!!
                if (position.z == world.storey.z && world.storey.isExplored(position) && !fov.isVisible(position)) {
                    spriteCache.getSprite(entity[GraphicsComponent.mapper]!!).apply {
                        setPosition(position.x.toFloat(), position.y.toFloat())
                        draw(batch)
                    }
                }
            }

            fogOfWar.forEach { position ->
                fogOfWarSprite.setPosition(position)
                fogOfWarSprite.draw(batch)
            }
        }
    }

    private fun computeTileGraphics() {
        if (!shouldComputeTileGraphics) return

        shouldComputeTileGraphics = false
        tileGraphics.clear()

        for (x in 0 until world.storey.width) {
            for (y in 0 until world.storey.height) {
                val tile = world.storey.tiles[x][y]
                if (tile.terrain === Terrain.FLOOR) {
                    tileGraphics.add(TileGraphic(tile.position, FLOOR))
                    continue
                }

                var tileNumber = 0
                if (world.storey.getNeighbor(tile, Direction.NORTH).terrain == Terrain.WALL) tileNumber += 1
                if (world.storey.getNeighbor(tile, Direction.EAST).terrain == Terrain.WALL) tileNumber += 2
                if (world.storey.getNeighbor(tile, Direction.SOUTH).terrain == Terrain.WALL) tileNumber += 4
                if (world.storey.getNeighbor(tile, Direction.WEST).terrain == Terrain.WALL) tileNumber += 8

                if (tileNumber != 15) tileGraphics.add(TileGraphic(tile.position, "$WALL$tileNumber"))

                if (world.storey.getNeighbor(tile, Direction.NORTH_WEST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, NORTH_WEST_CORNER, -1))
                if (world.storey.getNeighbor(tile, Direction.NORTH_EAST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, NORTH_EAST_CORNER, -1))
                if (world.storey.getNeighbor(tile, Direction.SOUTH_EAST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, SOUTH_EAST_CORNER, -1))
                if (world.storey.getNeighbor(tile, Direction.SOUTH_WEST).terrain == Terrain.FLOOR)
                    tileGraphics.add(TileGraphic(tile.position, SOUTH_WEST_CORNER, -1))
            }
        }
        tileGraphics.sortBy { it.priority }
    }
}