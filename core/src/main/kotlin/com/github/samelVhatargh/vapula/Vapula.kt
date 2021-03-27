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
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.*
import com.github.samelVhatargh.vapula.systems.commands.Attack
import com.github.samelVhatargh.vapula.systems.commands.Kill
import com.github.samelVhatargh.vapula.systems.commands.Move
import com.github.samelVhatargh.vapula.systems.commands.MoveOrAttack
import com.github.samelVhatargh.vapula.ui.createSkin
import com.github.samelVhatargh.vapula.utility.random
import com.strongjoshua.console.GUIConsole
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.ashley.get

class Vapula(private val debugArguments: DebugArguments) : KtxGame<KtxScreen>() {

    private var camera = OrthographicCamera()
    private var viewport = FitViewport(16f, 9f, camera)
    private val batch by lazy { SpriteBatch() }
    private lateinit var spriteAtlas: TextureAtlas

    private val engine = PooledEngine()

    private lateinit var console: GUIConsole

    override fun create() {
        Gdx.app.logLevel = debugArguments.logLevel
        val seed = debugArguments.getSeed()
        if (seed !== null) {
            random.setSeed(seed)
        }
        spriteAtlas = TextureAtlas(Gdx.files.internal("graphics/sprites.atlas"))

        val world = World(engine, spriteAtlas)


        val gameState = GameState()

        createSkin()

        val inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer

        val gameMap = world.gamMap[GameMap.mapper]!!

        engine.apply {
            addSystem(EnemyTurns(gameState, PathFinder(gameMap)))
            addSystem(PlayerInput(inputMultiplexer, world.player, gameState))
            addSystem(Move())
            addSystem(Attack())
            addSystem(MoveOrAttack())
            addSystem(Kill(spriteAtlas))
            addSystem(Camera(camera, inputMultiplexer))
            addSystem(MapRender(spriteAtlas, batch, world.player, world.gamMap))
            addSystem(FieldOfViewCalculator(world.player, world.gamMap))
            addSystem(Render(batch, viewport, world.player))
        }

        val commandExecutor =
            DebugCommandExecutor(inputMultiplexer, viewport.camera, world.gamMap, world.player, engine)
        console = GUIConsole().apply {
            setCommandExecutor(commandExecutor)
            displayKeyID = Input.Keys.GRAVE
        }

        debugArguments.getStartupCommands().forEach { command ->
            console.execCommand(command)
        }

        addScreen(GameScreen(engine, viewport, console, inputMultiplexer))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        console.dispose()
        batch.dispose()
        spriteAtlas.dispose()
    }
}