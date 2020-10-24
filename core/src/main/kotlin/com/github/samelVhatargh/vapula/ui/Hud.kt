package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.samelVhatargh.vapula.components.Stats
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class Hud {

    private var hp: Label

    val panel = scene2d.table {
        table {
            right().top().cell(growY = true)

            hp = label("HP: ") { cell ->
                cell.width(4 * 64f - 2 * 8f).pad(8f)
            }
        }

        right()
        setFillParent(true)
    }

    fun updatePlayerStats(stats: Stats) {
        hp.setText("HP: ${stats.hp}/${stats.maxHp}")
    }
}