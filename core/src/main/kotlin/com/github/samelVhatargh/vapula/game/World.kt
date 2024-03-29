package com.github.samelVhatargh.vapula.game

import com.badlogic.ashley.core.Engine
import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.entities.GoblinType
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.Storey
import com.github.samelVhatargh.vapula.map.generators.BSPDungeon
import com.github.samelVhatargh.vapula.map.generators.Map
import com.github.samelVhatargh.vapula.map.MapRenderSystem
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.getSystem

private const val MAP_WIDTH = 16 * 2
private const val MAP_HEIGHT = 16 * 2

private const val MAX_GOBLINS_PER_ROOM = 3
private const val MAX_BARRELS_PER_ROOM = 4

class World(private val engine: Engine) {

    private val maps =
        arrayOf(
            BSPDungeon().generate(MAP_WIDTH, MAP_HEIGHT),
            BSPDungeon().generate(MAP_WIDTH, MAP_HEIGHT),
            BSPDungeon().generate(MAP_WIDTH, MAP_HEIGHT)
        )

    val stories = Array(maps.size) { z -> Storey(maps[z], z) }
    var storey = stories[0]

    val entityFactory = Factory(engine, storey)

    val player = entityFactory.createPlayer()

    init {
        for (z in maps.indices) {
            val map = maps[z]
            entityFactory.storey = stories[z]
            fillMap(map, z)
            if (z != 0) {
                entityFactory.createStairs(false)
            }
            if (z != maps.size - 1) {
                entityFactory.createStairs(true)
            }
        }
    }

    fun changeStory(z: Int) {
        storey = stories[z]
        engine.getSystem<MapRenderSystem>().shouldComputeTileGraphics = true
    }

    private fun fillMap(map: Map, z: Int) {
        val rooms = map.rooms
        val tunnels = map.tunnels
        val tunnelPositions = mutableSetOf<PositionComponent>()
        tunnels.forEach {
            tunnelPositions.addAll(it.tiles.map { position ->
                PositionComponent(position.x, position.y, z)
            })
        }

        rooms.forEach { room ->
            val barrelCount = random.range(0..MAX_BARRELS_PER_ROOM)
            var barrelPosition = room.getRandomCorner()

            repeat(barrelCount) {
                if (!tunnelPositions.contains(barrelPosition)
                    && engine.getEntityAtPosition(barrelPosition, OCCUPY_SPACE_FAMILY) == null
                ) {
                    entityFactory.createBarrel(barrelPosition)
                    barrelPosition += random.collection(
                        listOf(
                            Direction.NORTH,
                            Direction.SOUTH,
                            Direction.EAST,
                            Direction.WEST
                        )
                    )
                }
            }

            val goblinCount = random.range(0..MAX_GOBLINS_PER_ROOM)

            repeat(goblinCount) {
                val type = random.collection(
                    listOf(
                        GoblinType.FIGHTER,
                        GoblinType.FIGHTER,
                        GoblinType.FIGHTER,
                        GoblinType.ARCHER,
                        GoblinType.ARCHER,
                        GoblinType.SHAMAN,
                    )
                )
                entityFactory.createGoblin(room.getRandomPosition(), type)
            }
        }

        tunnelPositions.forEach {
            var tileNumber = 0
            if (tunnelPositions.contains(it + Direction.NORTH)) {
                tileNumber += 1
            }
            if (tunnelPositions.contains(it + Direction.EAST)) {
                tileNumber += 2
            }
            if (tunnelPositions.contains(it + Direction.SOUTH)) {
                tileNumber += 4
            }
            if (tunnelPositions.contains(it + Direction.WEST)) {
                tileNumber += 8
            }
            entityFactory.createTunnel(it, "Railroad$tileNumber")
        }
    }
}