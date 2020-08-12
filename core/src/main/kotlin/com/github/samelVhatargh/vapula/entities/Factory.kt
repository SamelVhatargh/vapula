package com.github.samelVhatargh.vapula.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.map.Terrain
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

class Factory(private val engine: Engine, private val spriteAtlas: TextureAtlas, private val map: Entity) {

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
            with<Stats> {
                maxHp = 10 + (1..10).random() + (1..10).random()
                hp = maxHp
                damageDice = 8
            }
        }

        player.add(position)

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
            with<Stats> {
                maxHp = (1..6).random() + (1..6).random()
                hp = maxHp
                damageDice = 6
            }
        }
        monster.add(position)

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