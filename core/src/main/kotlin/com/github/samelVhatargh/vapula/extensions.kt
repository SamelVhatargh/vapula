package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.events.Notifier
import ktx.ashley.get

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

/**
 * Уведомляющий о событиях
 */
val Engine.notifier: Notifier by lazy { Notifier() }