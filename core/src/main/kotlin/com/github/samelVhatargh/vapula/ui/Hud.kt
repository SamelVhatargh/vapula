package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.github.samelVhatargh.vapula.components.Name
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.*
import ktx.actors.onClick
import ktx.ashley.get
import ktx.ashley.has
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane
import ktx.scene2d.table

private const val MAX_MESSAGE_COUNT = 100

const val HUD_WIDTH = 4

/**
 * Панель с различной игровой информацией в правой стороне экрана
 */
class Hud : Observer {

    private var hp: Label

    var messageScrollPane: ScrollPane
    private var messageLog: Label

    private val messages = mutableListOf<String>()

    val panel = scene2d.table {
        table {
            defaults().pad(8f).left().expandX()
            right().top().cell(growY = true, width = HUD_WIDTH * 64f)

            hp = label("HP: ")
            row()
            messageScrollPane = scrollPane { cell ->
                cell.height(4 * 64f - 2).expandY().fillX().bottom()

                messageLog = label("") {
                    wrap = true
                    fontScaleX = 15 / 24f
                    fontScaleY = 15 / 24f
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

    /**
     * Обновляет [показатели][stats] игрока
     */
    fun updatePlayerStats(stats: Stats) {
        hp.setText("HP: ${stats.hp}/${stats.maxHp}")
    }

    /**
     * Добавляет [сообщение][message] в лог сообщений
     */
    fun logMessage(message: String) {
        if (messages.size >= MAX_MESSAGE_COUNT) {
            messages.removeAt(0)
        }
        messages.add(message.capitalize())

        messageLog.setText(messages.joinToString("\r\n"))
        messageScrollPane.scrollTo(0f, 0f, 0f, 0f)
    }

    override fun onNotify(event: Event) {
        when (event) {
            is EntityDamaged -> {
                if (event.victim.has(Player.mapper) && event.victim.has(Stats.mapper)) {
                    updatePlayerStats(event.victim[Stats.mapper]!!)
                }
                logMessage("${event.victim[Name.mapper]!!.name} takes ${event.damage} damage")
            }
            is EntityHealed -> {
                logMessage("${event.healer[Name.mapper]!!.name} heals ${event.target[Name.mapper]!!.name} for  ${event.hp} damage")
            }
            is EntityAttacked -> {
                val word = if (event.miss) "misses" else "attacks"
                logMessage("${event.attacker[Name.mapper]!!.name} $word ${event.defender[Name.mapper]!!.name}")
            }
            is EntityDied -> {
                logMessage("${event.victim[Name.mapper]!!.name} dies")
            }
        }
    }

    override fun getSupportedTypes(): Array<EventType> =
        arrayOf(EventType.ENTITY_DAMAGED, EventType.ENTITY_DIED, EventType.ENTITY_ATTACKED)
}