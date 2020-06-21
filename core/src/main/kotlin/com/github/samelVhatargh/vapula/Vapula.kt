package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_ERROR
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.map.FieldOfView
import com.github.samelVhatargh.vapula.map.GameMap
import com.github.samelVhatargh.vapula.map.generators.DrunkardWalkDungeon
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.Camera
import com.github.samelVhatargh.vapula.systems.Move
import com.github.samelVhatargh.vapula.systems.PlayerInput
import com.github.samelVhatargh.vapula.systems.Render
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with

class Vapula(private val debugLevel: Int = LOG_ERROR) : KtxGame<KtxScreen>() {

    private var viewport = FitViewport(16f, 9f)
    private val batch by lazy { SpriteBatch() }

    private val engine = PooledEngine()

    override fun create() {
        Gdx.app.logLevel = debugLevel
        val sprites = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

        val map = GameMap(16 * 2, 9 * 2)
        map.generate(DrunkardWalkDungeon())

        val playerPosition = map.getRandomFloorTilePosition()
        val player = engine.entity {
            with<Graphics> {
                setSpriteRegion(sprites.findRegion("character"))
            }
            with<Player>()
        }
        player.add(playerPosition)

        val fov = FieldOfView(map).apply { update(player) }

        engine.apply {
            addSystem(Move(map, fov))
            addSystem(Camera(viewport.camera))
            addSystem(Render(batch, viewport))
        }


        Gdx.input.inputProcessor = PlayerInput(player, map, viewport.camera)

        addScreen(GameScreen(engine, viewport, sprites, batch, map, fov))
        setScreen<GameScreen>()
    }
}