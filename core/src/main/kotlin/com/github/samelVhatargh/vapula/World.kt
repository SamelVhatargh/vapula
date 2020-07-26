package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.map.generators.BSPDungeon
import ktx.ashley.entity
import ktx.ashley.with


class World(engine: Engine, spriteAtlas: TextureAtlas) {

    private val mapWidth = 16 * 2
    private val mapHeight = 16 * 2

    private val map = BSPDungeon().generate(mapWidth, mapHeight)

    val gamMap: Entity = engine.entity {
        with<GameMap> {
            width = mapWidth
            height = mapHeight
            tiles = map.tiles
        }
    }

    private val entityFactory = Factory(engine, spriteAtlas, gamMap)

    val player = entityFactory.createPlayer()

    init {
        entityFactory.createGoblin()
        entityFactory.createGoblin()
        entityFactory.createGoblin()
    }
}