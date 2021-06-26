package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.GameMap
import com.github.samelVhatargh.vapula.map.Path
import com.github.samelVhatargh.vapula.systems.commands.effects.ChangePosition
import com.github.samelVhatargh.vapula.systems.commands.effects.Effect
import ktx.ashley.get

abstract class BaseMove : Command {
    fun changePosition(engine: Engine, gameMap: GameMap, entity: Entity, newPosition: Position): Array<Effect> {
        val obstacle = engine.getEntityAtPosition(newPosition, OCCUPY_SPACE_FAMILY)

        if (obstacle == null && gameMap.isWalkable(newPosition.x, newPosition.y)) {
            return arrayOf(ChangePosition(entity, newPosition))
        }

        return emptyArray()
    }
}

class MoveInDirection(
    private val engine: Engine,
    private val entity: Entity,
    private val direction: Direction,
    private val gameMap: GameMap
) : BaseMove() {

    override fun execute(): Array<Effect> {
        val position = entity[Position.mapper]!!

        val newX = position.x + direction.x
        val newY = position.y + direction.y

        return changePosition(engine, gameMap, entity, Position(newX, newY))
    }
}

class MoveInPath(
    private val engine: Engine,
    private val entity: Entity,
    private val path: Path,
    private val gameMap: GameMap
) : BaseMove() {
    override fun execute(): Array<Effect> {
        val currentPosition = entity[Position.mapper]!!
        val destination = path.getNextPosition(currentPosition)

        return changePosition(engine, gameMap, entity, destination)
    }
}
