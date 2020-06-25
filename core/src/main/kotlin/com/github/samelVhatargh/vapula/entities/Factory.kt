package com.github.samelVhatargh.vapula.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.*
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
            with<Stats>{
                maxHp = (1..6).random() + (1..6).random()
                hp = maxHp
                damageDice = 6
            }
        }
        monster.add(position)

        return monster
    }

    private fun getRandomEmptyPosition(): Position = map[GameMap.mapper]!!.getRandomFloorTilePosition()
}