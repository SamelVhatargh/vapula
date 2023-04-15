package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Direction
import ktx.ashley.get

/**
 * Changes entity position
 */
class ChangePosition(
    private val entity: Entity,
    private val newPosition: Position,
) : Command {
    override fun execute(): Boolean {
        val position = entity[Position.mapper]!!

        changeDirection(position)

        position.x = newPosition.x
        position.y = newPosition.y
        position.z = newPosition.z
        entity[FieldOfView.mapper]?.shouldUpdate = true

        return false
    }

    private fun changeDirection(oldPosition: Position) {
        val graphics = entity[Graphics.mapper]
        if (graphics != null) {
            if (oldPosition.x < newPosition.x) {
                graphics.direction = Direction.EAST
            }
            if (oldPosition.x > newPosition.x) {
                graphics.direction = Direction.WEST
            }
        }
    }
}