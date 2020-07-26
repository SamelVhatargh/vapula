package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.map.generators.BSPDungeon
import ktx.ashley.entity
import ktx.ashley.with

private const val MAP_WIDTH = 16 * 2
private const val MAP_HEIGHT = 16 * 2

private const val MAX_GOBLINS = 3
private const val MAX_BARRELS = 10

class World(engine: Engine, spriteAtlas: TextureAtlas) {

    private val map = BSPDungeon().generate(MAP_WIDTH, MAP_HEIGHT)

    val gamMap: Entity = engine.entity {
        with<GameMap> {
            width = MAP_WIDTH
            height = MAP_HEIGHT
            tiles = map.tiles
        }
    }

    private val entityFactory = Factory(engine, spriteAtlas, gamMap)

    val player = entityFactory.createPlayer()

    init {
        repeat(MAX_BARRELS) {
            entityFactory.createBarrel()
        }

        repeat(MAX_GOBLINS) {
            entityFactory.createGoblin()
        }
    }
}