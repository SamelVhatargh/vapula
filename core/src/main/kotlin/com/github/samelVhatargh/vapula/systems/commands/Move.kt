package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.Path
import com.github.samelVhatargh.vapula.map.Storey
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get

abstract class BaseMove : Command {
    fun changePosition(engine: Engine, storey: Storey, entity: Entity, newPosition: Position) {
        val obstacle = engine.getEntityAtPosition(newPosition, OCCUPY_SPACE_FAMILY)

        if (obstacle == null && storey.isWalkable(newPosition.x, newPosition.y)) {
            ChangePosition(entity, newPosition).execute()
        }
    }
}

class MoveInDirection(
    private val engine: Engine,
    private val entity: Entity,
    private val direction: Direction,
    private val storey: Storey
) : BaseMove() {

    override fun execute() {
        val position = entity[Position.mapper]!!

        val newX = position.x + direction.x
        val newY = position.y + direction.y

        changePosition(engine, storey, entity, Position(newX, newY, position.z))
    }
}

class MoveInPath(
    private val engine: Engine,
    private val entity: Entity,
    private val path: Path,
    private val storey: Storey
) : BaseMove() {
    override fun execute() {
        val currentPosition = entity[Position.mapper]!!
        val destination = path.getNextPosition(currentPosition)

        changePosition(engine, storey, entity, destination)
    }
}

class AggressiveMove(
    private val engine: Engine,
    private val entity: Entity,
    private val direction: Direction,
    private val storey: Storey
) : BaseMove() {
    override fun execute() {
        val entityPosition = entity[Position.mapper]!!
        val targetPosition = Position(entityPosition.x + direction.x, entityPosition.y + direction.y, entityPosition.z)
        val target = engine.getEntityAtPosition(targetPosition, allOf(Stats::class).exclude(Dead::class).get())
            ?: return MoveInDirection(engine, entity, direction, storey).execute()

        return Attack(engine.notifier, entity, target).execute()
    }
}
