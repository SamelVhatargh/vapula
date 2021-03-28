package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.GameMap
import com.github.samelVhatargh.vapula.map.generators.BSPDungeon
import com.github.samelVhatargh.vapula.utility.random

private const val MAP_WIDTH = 16 * 2
private const val MAP_HEIGHT = 16 * 2

private const val MAX_GOBLINS_PER_ROOM = 3
private const val MAX_BARRELS_PER_ROOM = 4

class World(engine: Engine, spriteAtlas: TextureAtlas) {

    private val map = BSPDungeon().generate(MAP_WIDTH, MAP_HEIGHT)

    val gameMap = GameMap(map)

    private val entityFactory = Factory(engine, spriteAtlas, gameMap)

    val player = entityFactory.createPlayer()

    init {
        val rooms = map.rooms
        val tunnels = map.tunnels
        val tunnelPositions = mutableSetOf<Position>()
        tunnels.forEach {
            tunnelPositions.addAll(it.tiles)
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
                entityFactory.createGoblin(room.getRandomPosition())
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