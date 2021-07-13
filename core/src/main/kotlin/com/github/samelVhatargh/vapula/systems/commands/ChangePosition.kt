package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Animation
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.components.WalkAnimation
import ktx.ashley.get

/**
 * Changes entity position
 */
class ChangePosition(
    private val entity: Entity,
    private val newPosition: Position,
    private val animate: Boolean = true
) : Command {
    override fun execute() {
        val position = entity[Position.mapper]!!
        if (animate) {
            val animation = Animation(WalkAnimation(position, newPosition))
            entity.add(animation)
        }
        position.x = newPosition.x
        position.y = newPosition.y
        position.z = newPosition.z
        entity[FieldOfView.mapper]?.shouldUpdate = true
    }
}