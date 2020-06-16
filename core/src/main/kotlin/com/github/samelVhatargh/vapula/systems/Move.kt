package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.components.MoveDirection
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.remove

class Move : IteratingSystem(
    allOf(MoveDirection::class, Position::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[Position.mapper]!!
        val move = entity[MoveDirection.mapper]!!

        position.x += move.direction.x.toInt()
        position.y += move.direction.y.toInt()
        
        entity.remove<MoveDirection>()
    }
}