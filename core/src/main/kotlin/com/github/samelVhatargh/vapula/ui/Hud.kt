package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.samelVhatargh.vapula.components.Player
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.EntityDamaged
import com.github.samelVhatargh.vapula.events.Event
import com.github.samelVhatargh.vapula.events.Observer
import ktx.ashley.get
import ktx.ashley.has
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

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

    fun updatePlayerStats(stats: Stats) {
        hp.setText("HP: ${stats.hp}/${stats.maxHp}")
    }

    override fun onNotify(event: Event) {
        when (event) {
            is EntityDamaged -> {
                if (event.victim.has(Player.mapper) && event.victim.has(Stats.mapper)) {
                    updatePlayerStats(event.victim[Stats.mapper]!!)
                }
            }
        }
    }
}