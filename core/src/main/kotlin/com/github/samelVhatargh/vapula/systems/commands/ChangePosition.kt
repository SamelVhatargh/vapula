package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.get

/**
 * Changes entity position
 */
class ChangePosition(private val entity: Entity, private val newPosition: Position) : Command {
    override fun execute() {
        val position = entity[Position.mapper]!!
        position.x = newPosition.x
        position.y = newPosition.y
        entity[FieldOfView.mapper]?.shouldUpdate = true
    }
}