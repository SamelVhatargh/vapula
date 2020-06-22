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
                setSpriteRegion(spriteAtlas.findRegion("character"))
            }
            with<Player>()
            with<OccupySpace>()
            with<FieldOfView>()
        }

        player.add(position)

        return player
    }

    fun createGoblin(position: Position = getRandomEmptyPosition()): Entity {
        val monster = engine.entity {
            with<Graphics> {
                setSpriteRegion(spriteAtlas.findRegion("goblin"))
            }
            with<OccupySpace>()
        }
        monster.add(position)

        return monster
    }

    private fun getRandomEmptyPosition(): Position = map[GameMap.mapper]!!.getRandomFloorTilePosition()
}