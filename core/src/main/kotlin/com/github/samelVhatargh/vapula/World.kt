package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.entities.GoblinType
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.Storey
import com.github.samelVhatargh.vapula.map.generators.BSPDungeon
import com.github.samelVhatargh.vapula.map.generators.Map
import com.github.samelVhatargh.vapula.utility.random

private const val MAP_WIDTH = 16 * 2
private const val MAP_HEIGHT = 16 * 2

private const val MAX_GOBLINS_PER_ROOM = 3
private const val MAX_BARRELS_PER_ROOM = 4

class World(engine: Engine) {

    private val maps =
        arrayOf(BSPDungeon().generate(MAP_WIDTH, MAP_HEIGHT), BSPDungeon().generate(MAP_WIDTH, MAP_HEIGHT))

    val storey = Storey(maps[0], 0)

    private val entityFactory = Factory(engine, storey)

    val player = entityFactory.createPlayer()

    init {
        for (z in maps.indices) {
            val map = maps[z]
            if (z > 0) {
                entityFactory.storey = Storey(map, z)
            }
            fillMap(map, z)
        }
    }

    private fun fillMap(map: Map, z: Int) {
        val rooms = map.rooms
        val tunnels = map.tunnels
        val tunnelPositions = mutableSetOf<Position>()
        tunnels.forEach {
            tunnelPositions.addAll(it.tiles.map { position ->
                Position(position.x, position.y, z)
            })
        }

        rooms.forEach { room ->
            val barrelCount = random.range(0..MAX_BARRELS_PER_ROOM)
            var barrelPosition = room.getRandomCorner()

            repeat(barrelCount) {
                if (!tunnelPositions.contains(barrelPosition)) {
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