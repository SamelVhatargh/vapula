package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.map.FieldOfViewComponent
import com.github.samelVhatargh.vapula.graphics.GraphicsComponent
import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.map.Direction
import ktx.ashley.get

/**
 * Changes entity position
 */
class ChangePosition(
    private val entity: Entity,
    private val newPosition: PositionComponent,
) : Command {
    override fun execute(): Boolean {
        val position = entity[PositionComponent.mapper]!!

        changeDirection(position)

        position.x = newPosition.x
        position.y = newPosition.y
        position.z = newPosition.z
        entity[FieldOfViewComponent.mapper]?.shouldUpdate = true

        return false
    }

    private fun changeDirection(oldPosition: PositionComponent) {
        val graphics = entity[GraphicsComponent.mapper]
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