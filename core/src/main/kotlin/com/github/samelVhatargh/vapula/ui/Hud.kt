package com.github.samelVhatargh.vapula.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.github.samelVhatargh.vapula.components.Stats
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class Hud {

    private var hp: Label

    val panel = scene2d.table {
        defaults().fillX().expandX()
        hp = label("HP: ") {
            setAlignment(Align.center)
        }

        top()
        setFillParent(true)
        pack()
    }

    fun updatePlayerStats(stats: Stats) {
        hp.setText("HP: ${stats.hp}/${stats.maxHp}")
    }
}