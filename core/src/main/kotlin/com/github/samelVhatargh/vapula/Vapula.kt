package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_ERROR
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.console.DebugCommandExecutor
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.map.generators.DrunkardWalkDungeon
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.*
import com.github.samelVhatargh.vapula.systems.commands.Attack
import com.github.samelVhatargh.vapula.systems.commands.Kill
import com.github.samelVhatargh.vapula.systems.commands.Move
import com.github.samelVhatargh.vapula.systems.commands.MoveOrAttack
import com.strongjoshua.console.GUIConsole
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with

class Vapula(private val debugLevel: Int = LOG_ERROR) : KtxGame<KtxScreen>() {

    private var viewport = FitViewport(16f, 9f)
    private val batch by lazy { SpriteBatch() }
    private lateinit var spriteAtlas: TextureAtlas

    private val engine = PooledEngine()

    private lateinit var console: GUIConsole

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
        val entityFactory = Factory(engine, spriteAtlas, map)

        val player = entityFactory.createPlayer()
        entityFactory.createGoblin()
        entityFactory.createGoblin()
        entityFactory.createGoblin()

        val gameState = GameState()

        val inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer

        engine.apply {
            addSystem(EnemyTurns(gameState))
            addSystem(PlayerInput(inputMultiplexer, player, gameState))
            addSystem(Move())
            addSystem(Attack())
            addSystem(MoveOrAttack())
            addSystem(Kill(spriteAtlas))
            addSystem(Camera(viewport.camera))
            addSystem(MapRender(spriteAtlas, batch, player, map))
            addSystem(FieldOfViewCalculator(player, map))
            addSystem(Render(batch, viewport, player))
        }

        console = GUIConsole().apply {
            setCommandExecutor(DebugCommandExecutor(inputMultiplexer, viewport.camera, map, player, engine))
            displayKeyID = Input.Keys.GRAVE
        }

        addScreen(GameScreen(engine, viewport, console))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        console.dispose()
        batch.dispose()
        spriteAtlas.dispose()
    }
}