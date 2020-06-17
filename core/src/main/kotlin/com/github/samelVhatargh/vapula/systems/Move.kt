package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.components.MoveDirection
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.GameMap
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.remove

class Move(private val gameMap: GameMap) : IteratingSystem(
    allOf(MoveDirection::class, Position::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!
        val move = entity[MoveDirection.mapper]!!

        val newX = position.x + move.direction.x.toInt()
        val newY = position.y + move.direction.y.toInt()

        if (gameMap.isWalkable(newX, newY)) {
            position.x = newX
            position.y = newY
        }

        entity.remove<MoveDirection>()
    }
}