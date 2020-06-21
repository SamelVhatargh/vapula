package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_ERROR
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.map.generators.DrunkardWalkDungeon
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.*
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

class Vapula(private val debugLevel: Int = LOG_ERROR) : KtxGame<KtxScreen>() {

    private var viewport = FitViewport(16f, 9f)
    private val batch by lazy { SpriteBatch() }
    private lateinit var spriteAtlas: TextureAtlas

    private val engine = PooledEngine()

    override fun create() {
        Gdx.app.logLevel = debugLevel
        spriteAtlas = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

        val map = engine.entity {
            with<GameMap> {
                width = 16 * 2
                height = 9 * 2
                tiles = DrunkardWalkDungeon().getTiles(width, height)
            }
        }

        val fov = FieldOfView()

        val playerPosition = map[GameMap.mapper]?.getRandomFloorTilePosition()
        val player = engine.entity {
            with<Graphics> {
                setSpriteRegion(spriteAtlas.findRegion("character"))
            }
            with<Player>()
        }
        player.add(playerPosition)
        player.add(fov)

        engine.apply {
            addSystem(Move(map))
            addSystem(FieldOfVieCalculator(player, map))
            addSystem(Camera(viewport.camera))
            addSystem(MapRender(spriteAtlas, batch, player, map))
            addSystem(Render(batch, viewport))
        }


        Gdx.input.inputProcessor = PlayerInput(player, map, viewport.camera)

        addScreen(GameScreen(engine, viewport))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        spriteAtlas.dispose()
    }
}