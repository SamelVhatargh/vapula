package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Direction
import ktx.ashley.get

private val directions = listOf(
    Direction.NORTH,
    Direction.SOUTH,
    Direction.EAST,
    Direction.WEST,
    Direction.NORTH_EAST,
    Direction.NORTH_WEST,
    Direction.SOUTH_EAST,
    Direction.SOUTH_WEST
)

class Wander(private val engine: Engine, private val entity: Entity, private val world: World) : Command {
    override fun execute() {
        val z = entity[Position.mapper]!!.z
        MoveInDirection(engine, entity, directions.random(), world.stories[z]).execute()
    }
}