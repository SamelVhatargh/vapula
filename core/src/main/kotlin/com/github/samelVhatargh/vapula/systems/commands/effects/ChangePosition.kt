package com.github.samelVhatargh.vapula.systems.commands.effects

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.get

/**
 * changes entity position
 */
class ChangePosition(private val entity: Entity, private val newPosition: Position) : Effect {
    override fun apply(): Array<Effect> {
        val position = entity[Position.mapper]!!
        position.x = newPosition.x
        position.y = newPosition.y
        entity[FieldOfView.mapper]?.shouldUpdate = true

        return emptyArray()
    }
}