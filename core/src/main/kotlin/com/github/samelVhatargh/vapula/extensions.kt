package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.events.Notifier
import com.github.samelVhatargh.vapula.ui.CloseDialogButton
import com.github.samelVhatargh.vapula.ui.ModalDialogComponent
import com.github.samelVhatargh.vapula.ui.ModalDialogButton
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

fun Sprite.setPosition(position: Position) {
    setPosition(position.x.toFloat(), position.y.toFloat())
}

/**
 * Возвращает первую попавшуюся сущность в указанном месте из указанного семейства
 */
fun Engine.getEntityAtPosition(position: Position, family: Family): Entity? {
    val entities = getEntitiesFor(family)
    return entities.find {
        it[Position.mapper] == position
    }
}

fun Engine.addModalDialogWindow(
    modalTitle: String,
    modalText: String,
    modalButtons: Array<ModalDialogButton> = arrayOf(CloseDialogButton(this))
) {
    entity {
        with<ModalDialogComponent> {
            title = modalTitle
            text = modalText
            buttons = modalButtons
        }
    }
}

/**
 * Уведомляющий о событиях
 */
val Engine.notifier: Notifier by lazy { Notifier() }