package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.console.DebugArguments
import com.github.samelVhatargh.vapula.console.DebugCommandExecutor
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.map.generators.BSPDungeon
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

class Vapula(private val debugArguments: DebugArguments) : KtxGame<KtxScreen>() {

    private var camera = OrthographicCamera()
    private var viewport = FitViewport(16f, 9f, camera)
    private val batch by lazy { SpriteBatch() }
    private lateinit var spriteAtlas: TextureAtlas

    private val engine = PooledEngine()

    private lateinit var console: GUIConsole

    override fun create() {
        Gdx.app.logLevel = debugArguments.logLevel
        spriteAtlas = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

        val map = engine.entity {
            with<GameMap> {
                width = 16 * 2
                height = 16 * 2
                tiles = BSPDungeon().generate(width, height).tiles
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
            addSystem(Camera(camera, inputMultiplexer))
            addSystem(MapRender(spriteAtlas, batch, player, map))
            addSystem(FieldOfViewCalculator(player, map))
            addSystem(Render(batch, viewport, player))
        }

        val commandExecutor = DebugCommandExecutor(inputMultiplexer, viewport.camera, map, player, engine)
        console = GUIConsole().apply {
            setCommandExecutor(commandExecutor)
            displayKeyID = Input.Keys.GRAVE
        }

        commandExecutor.validStartupCommands.forEach { command ->
            if (debugArguments.have(command)) {
                console.execCommand(command)
            }
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