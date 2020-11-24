package com.github.samelVhatargh.vapula.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger

class Factory(private val engine: Engine, private val spriteAtlas: TextureAtlas, private val map: Entity) {

    companion object {
        val log = logger<Factory>()
    }

    fun createPlayer(position: Position = getRandomEmptyPosition()): Entity {
        val player = engine.entity {
            with<Graphics> {
                spriteName = "character"
                setSpriteRegion(spriteAtlas.findRegion(spriteName))
            }
            with<Player>()
            with<OccupySpace>()
            with<FieldOfView>()
            with<Name> {
                name = "player"
            }
        }

        player.add(position)

        val stats = Stats().apply {
            strength = random.dice("1d6 + 3")
            dexterity = random.dice("1d6 + 3")
            constitution = random.dice("1d6 + 3")
            perception = random.dice("1d6 + 3")
            intellegence = random.dice("1d6 + 3")
            wisdom = random.dice("1d6 + 3")
            charisma = random.dice("1d6 + 3")

            level = 1
            damageDice = 8
            generateHp(10, 10)
        }
        player.add(stats)
        log.debug { stats.toString() }

        return player
    }

    private var goblinCount = 0

    fun createGoblin(position: Position = getRandomEmptyPosition()): Entity {
        goblinCount++
        val monster = engine.entity {
            with<Graphics> {
                spriteName = "goblin"
                setSpriteRegion(spriteAtlas.findRegion(spriteName))
            }
            with<OccupySpace>()
            with<Name> {
                name = "Goblin $goblinCount"
            }
            with<Ai>()
        }
        monster.add(position)

        val stats = Stats().apply {
            strength = random.dice("1d2")
            dexterity = random.dice("1d6 + 1")
            constitution = random.dice("1d4 - 1")
            perception = random.dice("1d4")
            intellegence = random.dice("1d2")
            wisdom = random.dice("1d2")
            charisma = random.dice("1d4")

            level = 1
            damageDice = 4
            generateHp(2)
        }
        monster.add(stats)

        log.debug { stats.toString() }

        return monster
    }

    private fun getRandomEmptyPosition(): Position {
        val map = map[GameMap.mapper]!!
        val objects = engine.getEntitiesFor(OCCUPY_SPACE_FAMILY)

        var i = 1
        while (i < 1000) {
            val x = (0 until map.width).random()
            val y = (0 until map.height).random()

            if (map.tiles[x][y].terrain == Terrain.FLOOR) {
                val position = Position(x, y)

                if (objects.find {
                        it[Position.mapper]!! == position
                    } === null) return position
            }

            i++
        }

        return Position(5, 5)
    }

    fun createBarrel(position: Position = getRandomEmptyPosition()): Entity? {
        try {
            if (map[GameMap.mapper]!!.tiles[position.x][position.y].terrain == Terrain.WALL) {
                return null
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return null
        }

        val barrel = engine.entity {
            with<Graphics> {
                spriteName = "barrel"
                setSpriteRegion(spriteAtlas.findRegion(spriteName))
            }
            with<OccupySpace>()
        }
        barrel.add(position)

        return barrel
    }

    fun createTunnel(position: Position, sprite: String = "Railroad16") {
        val tunnel = engine.entity {
            with<Graphics> {
                spriteName = sprite
                layer = Layer.FLOOR
                var region = spriteAtlas.findRegion(spriteName)
                if (region === null) {
                    region = spriteAtlas.findRegion("x")
                }
                setSpriteRegion(region)
            }
            with<VisibleIfExploredAndOutOfFieldOfView>()
        }
        tunnel.add(position)
    }
}