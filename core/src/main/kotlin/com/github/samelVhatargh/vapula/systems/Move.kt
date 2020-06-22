package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.MoveDirection
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.remove

class Move(map: Entity) : IteratingSystem(
    allOf(MoveDirection::class, Position::class).get()
) {
    private val gameMap = map[GameMap.mapper]!!

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!
        val move = entity[MoveDirection.mapper]!!

        val newX = position.x + move.direction.x.toInt()
        val newY = position.y + move.direction.y.toInt()

        val entities = engine.getEntitiesFor(OCCUPY_SPACE_FAMILY)
        val obstacle = entities.find {
            val occupierPosition = it[Position.mapper]!!
            occupierPosition.x == newX && occupierPosition.y == newY
        }

        if (obstacle == null && gameMap.isWalkable(newX, newY)) {
            position.x = newX
            position.y = newY

            entity[FieldOfView.mapper]?.shouldUpdate = true
        }

        entity.remove<MoveDirection>()
    }
}