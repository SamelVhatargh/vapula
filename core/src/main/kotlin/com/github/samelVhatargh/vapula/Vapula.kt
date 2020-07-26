package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.console.DebugArguments
import com.github.samelVhatargh.vapula.console.DebugCommandExecutor
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.systems.*
import com.github.samelVhatargh.vapula.systems.commands.Attack
import com.github.samelVhatargh.vapula.systems.commands.Kill
import com.github.samelVhatargh.vapula.systems.commands.Move
import com.github.samelVhatargh.vapula.systems.commands.MoveOrAttack
import com.strongjoshua.console.GUIConsole
import ktx.app.KtxGame
import ktx.app.KtxScreen

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

        val world = World(engine, spriteAtlas)


        val gameState = GameState()

        val inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer

        engine.apply {
            addSystem(EnemyTurns(gameState))
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

        val commandExecutor = DebugCommandExecutor(inputMultiplexer, viewport.camera, world.gamMap, world.player, engine)
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