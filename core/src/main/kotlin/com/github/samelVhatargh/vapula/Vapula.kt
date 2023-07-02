package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.console.DebugArguments
import com.github.samelVhatargh.vapula.console.DebugConsole
import com.github.samelVhatargh.vapula.graphics.AnimationSystem
import com.github.samelVhatargh.vapula.graphics.IdleAnimationSystem
import com.github.samelVhatargh.vapula.graphics.RenderSystem
import com.github.samelVhatargh.vapula.screens.GameScreen
import com.github.samelVhatargh.vapula.screens.LoadingScreen
import com.github.samelVhatargh.vapula.sounds.MusicSystem
import com.github.samelVhatargh.vapula.sounds.SoundSystem
import com.github.samelVhatargh.vapula.systems.*
import com.github.samelVhatargh.vapula.ui.Hud
import com.github.samelVhatargh.vapula.ui.ModalDialogSystem
import com.github.samelVhatargh.vapula.graphics.SpriteCache
import com.github.samelVhatargh.vapula.ui.PlayerInputSystem
import com.github.samelVhatargh.vapula.utility.random
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Vapula(private val debugArguments: DebugArguments) : KtxGame<KtxScreen>() {

    private var camera = OrthographicCamera()
    private var viewport = FitViewport(16f, 9f, camera)
    private val batch by lazy { SpriteBatch() }
    private val assets by lazy { AssetManager() }

    private val engine = PooledEngine()

    override fun create() {
        Gdx.app.logLevel = debugArguments.logLevel
        val seed = debugArguments.getSeed()
        if (seed !== null) {
            random.setSeed(seed)
        }

        val world = World(engine)

        val inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer

        val spriteCache = SpriteCache(assets)
        val hud = Hud(inputMultiplexer)
        engine.apply {
            addSystem(PlayerInputSystem(inputMultiplexer, camera, world))
            addSystem(AnimationSystem(world))
            addSystem(SoundSystem(assets, world))
            addSystem(Camera(camera, inputMultiplexer))
            addSystem(MapRender(spriteCache, batch, viewport, world))
            addSystem(FieldOfViewCalculator(world))
            addSystem(IdleAnimationSystem())
            addSystem(RenderSystem(spriteCache, batch, viewport, world))
            addSystem(TurnLoop(world.player))
            addSystem(Ai(AiBrain(engine, world), world.player))
            addSystem(GameOver())
            addSystem(MusicSystem(assets))
            addSystem(hud)
            addSystem(ModalDialogSystem(inputMultiplexer))
            addSystem(DebugConsole(debugArguments, inputMultiplexer, viewport, world))
        }

        addScreen(GameScreen(engine, viewport))
        addScreen(LoadingScreen(assets, this, hud))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        assets.dispose()
    }
}