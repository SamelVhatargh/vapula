package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.samelVhatargh.vapula.components.Name
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.*
import ktx.ashley.get
import ktx.ashley.has
import ktx.log.debug
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

/**
 * Панель с различной игровой информацией в правой стороне экрана
 */
class Hud : Observer {

    private var hp: Label

    val panel = scene2d.table {
        table {
            right().top().cell(growY = true)

            hp = label("HP: ") { cell ->
                cell.width(4 * 64f - 2 * 8f).pad(8f)
            }

            background("background")
        }

        right()
        setFillParent(true)
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
        debug { message }
    }

    override fun onNotify(event: Event) {
        when (event) {
            is EntityDamaged -> {
                if (event.victim.has(Player.mapper) && event.victim.has(Stats.mapper)) {
                    updatePlayerStats(event.victim[Stats.mapper]!!)
                }
                logMessage("${event.victim[Name.mapper]!!.name} takes ${event.damage} damage")
            }
            is EntityAttacked -> {
                logMessage("${event.attacker[Name.mapper]!!.name} attacks ${event.defender[Name.mapper]!!.name}")
            }
            is EntityDied -> {
                logMessage("${event.victim[Name.mapper]!!.name} dies")
            }
        }
    }
}