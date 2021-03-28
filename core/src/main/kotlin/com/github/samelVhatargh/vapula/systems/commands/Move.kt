package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.GameMap
import com.github.samelVhatargh.vapula.map.Path
import ktx.ashley.get


class Move : EntitySystem() {
    fun execute(entity: Entity, direction: Direction, gameMap: GameMap) {
        val position = entity[Position.mapper]!!

        val newX = position.x + direction.x
        val newY = position.y + direction.y

        val obstacle = engine.getEntityAtPosition(Position(newX, newY), OCCUPY_SPACE_FAMILY)

        if (obstacle == null && gameMap.isWalkable(newX, newY)) {
            position.x = newX
            position.y = newY

            entity[FieldOfView.mapper]?.shouldUpdate = true
        }
    }

    fun execute(entity: Entity, path: Path, gameMap: GameMap) {
        val currentPosition = entity[Position.mapper]!!
        val destination = path.getNextPosition(currentPosition)

        val obstacle = engine.getEntityAtPosition(Position(destination.x, destination.y), OCCUPY_SPACE_FAMILY)

        if (obstacle == null && gameMap.isWalkable(destination.x, destination.y)) {
            currentPosition.x = destination.x
            currentPosition.y = destination.y

            entity[FieldOfView.mapper]?.shouldUpdate = true
        }
    }
}