package com.github.samelVhatargh.vapula.game.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.game.World
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import com.github.samelVhatargh.vapula.events.EntityMoved
import com.github.samelVhatargh.vapula.game.stats.StatsComponent
import com.github.samelVhatargh.vapula.game.statuses.Dead
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.Path
import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.map.Storey
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.*

abstract class BaseMove : Command {
    fun changePosition(engine: Engine, storey: Storey, entity: Entity, newPosition: PositionComponent): Boolean {
        val obstacle = engine.getEntityAtPosition(newPosition, OCCUPY_SPACE_FAMILY)

        if (obstacle == null && storey.isWalkable(newPosition.x, newPosition.y)) {
            val oldPosition = entity[PositionComponent.mapper]!!.clone()
            engine.notifier.notify(EntityMoved(entity, oldPosition, newPosition))
            return ChangePosition(entity, newPosition).execute()
        }

        return false
    }
}

class MoveInDirection(
    private val engine: Engine,
    private val entity: Entity,
    private val direction: Direction,
    private val storey: Storey
) : BaseMove() {

    override fun execute(): Boolean {
        val position = entity[PositionComponent.mapper]!!

        val newX = position.x + direction.x
        val newY = position.y + direction.y

        changePosition(engine, storey, entity, PositionComponent(newX, newY, position.z))

        return false
    }
}

class MoveInPath(
    private val engine: Engine,
    private val entity: Entity,
    private val path: Path,
    private val storey: Storey,
    private val continueToMoveInNextTurn: Boolean = false
) : BaseMove() {
    override fun execute(): Boolean {
        val currentPosition = entity[PositionComponent.mapper]!!
        val destination = path.getNextPosition(currentPosition)

        changePosition(engine, storey, entity, destination)

        if (currentPosition == path.getLastPosition()) {
            return false
        }

        return continueToMoveInNextTurn
    }
}

class AggressiveMove(
    private val engine: Engine,
    private val entity: Entity,
    private val direction: Direction,
    private val world: World
) : BaseMove() {
    override fun execute(): Boolean {
        val entityPosition = entity[PositionComponent.mapper]!!
        val targetPosition = PositionComponent(entityPosition.x + direction.x, entityPosition.y + direction.y, entityPosition.z)
        val target = engine.getEntityAtPosition(targetPosition, allOf(StatsComponent::class).exclude(Dead::class).get())
            ?: return MoveInDirection(engine, entity, direction, world.storey).execute()

        return Attack(engine, entity, target).execute()
    }
}
