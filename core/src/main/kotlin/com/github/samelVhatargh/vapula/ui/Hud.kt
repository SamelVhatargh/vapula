package com.github.samelVhatargh.vapula.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.components.Name
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.*
import com.github.samelVhatargh.vapula.notifier
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.has
import ktx.scene2d.*

private const val MAX_MESSAGE_COUNT = 100

const val HUD_WIDTH = 4

/**
 * All the styling of hud panel is described here
 */
private class HudView {

    private var hp: Label? = null
    private var messageScrollPane: ScrollPane? = null
    private var messageLog: Label? = null
    private val messages = mutableListOf<String>()
    val scrollFocus = messageScrollPane

    /**
     * Returns hud panel widget
     */
    fun create(): KTableWidget {
        return scene2d.table {
            table {
                defaults().pad(8f).left().expandX()
                right().top().cell(growY = true, width = HUD_WIDTH * 64f)

                hp = label("HP: ", LabelStyle.CAPTION.name)
                row()
                messageScrollPane = scrollPane { cell ->
                    cell.height(4 * 64f - 2).expandY().fillX().bottom()

                    messageLog = label("", LabelStyle.SMALL.name) {
                        wrap = true
                        setAlignment(Align.bottomLeft)
                    }
                }

                background("background")
            }

            right()
            setFillParent(true)
            onClick {
            }
        }
    }

    /**
     * Updates player [stats]
     */
    fun updatePlayerStats(stats: Stats) {
        hp?.setText("HP: ${stats.hp}/${stats.maxHp}")
    }

    /**
     * Adds [message] to message log
     */
    fun logMessage(message: String) {
        if (messages.size >= MAX_MESSAGE_COUNT) {
            messages.removeAt(0)
        }
        messages.add(message.capitalize())

        messageLog?.setText(messages.joinToString("\r\n"))
        messageScrollPane?.scrollTo(0f, 0f, 0f, 0f)
    }
}

/**
 * Panel with various game information on the right side of the screen
 */
class Hud(private val inputMultiplexer: InputMultiplexer) : EntitySystem(), Observer {

    private val stage: Stage by lazy {
        Stage(FitViewport(16 * 64f, 9 * 64f))
    }

    private val hudView = HudView()

    override fun addedToEngine(engine: Engine) {
        inputMultiplexer.addProcessor(0, stage)
        engine.notifier.addObserver(this)
    }

    override fun removedFromEngine(engine: Engine) {
        engine.notifier.removeObserver(this)
        inputMultiplexer.removeProcessor(stage)
        stage.dispose()
    }

    override fun update(deltaTime: Float) {
        stage.act()
        stage.draw()
    }

    /**
     * Adds hud panel with all necessary information to stage
     *
     * This should only be called after LoadingScreen have finished loading all of assets.
     */
    fun setupUI() {
        stage += hudView.create()
        stage.scrollFocus = hudView.scrollFocus
        val player = engine.getEntitiesFor(allOf(Player::class, Stats::class).get()).first()
        hudView.updatePlayerStats(player[Stats.mapper]!!)
    }

    override fun onNotify(event: Event) {
        when (event) {
            is EntityDamaged -> {
                if (event.victim.has(Player.mapper) && event.victim.has(Stats.mapper)) {
                    hudView.updatePlayerStats(event.victim[Stats.mapper]!!)
                }
                hudView.logMessage("${event.victim[Name.mapper]!!.name} takes ${event.damage} damage")
            }

            is EntityHealed -> {
                hudView.logMessage("${event.healer[Name.mapper]!!.name} heals ${event.target[Name.mapper]!!.name} for  ${event.hp} damage")
            }

            is EntityAttacked -> {
                val word = if (event.miss) "misses" else "attacks"
                hudView.logMessage("${event.attacker[Name.mapper]!!.name} $word ${event.defender[Name.mapper]!!.name}")
            }

            is EntityDied -> {
                hudView.logMessage("${event.victim[Name.mapper]!!.name} dies")
            }
        }
    }

    override fun getSupportedTypes(): Array<EventType> =
        arrayOf(EventType.ENTITY_DAMAGED, EventType.ENTITY_DIED, EventType.ENTITY_ATTACKED)
}