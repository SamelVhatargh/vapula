package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Bresenham2
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.systems.commands.Attack
import com.github.samelVhatargh.vapula.systems.commands.Command
import com.github.samelVhatargh.vapula.systems.commands.MoveInDirection
import com.github.samelVhatargh.vapula.systems.commands.MoveInPath
import ktx.ashley.get
import ktx.ashley.has

/**
 * Decides what a monster will do
 */
class AiBrain(private val engine: Engine, world: World) {
    private val player = world.player
    private val gameMap = world.gameMap
    private val pathFinder = PathFinder(gameMap, engine)

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

    fun getCommand(entity: Entity): Command {
        val playerPosition = player[Position.mapper]!!
        val monsterPosition = entity[Position.mapper]!!

        if (player.has(Dead.mapper)) {
            return wander(entity)
        }

        //if player is near - attack!
        if (playerPosition.isNeighbourTo(monsterPosition)) {
            return Attack(engine.notifier, entity, player)
        }

        //Run to player is he is visible
        if (isInLineOfSight(entity, player)) {
            val path = pathFinder.findPath(monsterPosition, playerPosition)
            if (path.isEmpty()) {
                return wander(entity)
            }

            return MoveInPath(engine, entity, path, gameMap)
        }

        return wander(entity)
    }

    private fun wander(entity: Entity): Command = MoveInDirection(engine, entity, directions.random(), gameMap)

    /**
     * Check if Entity can see another Entity
     */
    private fun isInLineOfSight(entity: Entity, target: Entity): Boolean {
        val start = entity[Position.mapper]!!
        val end = target[Position.mapper]!!
        val sightRange = entity[Stats.mapper]!!.sightRange

        val line = Bresenham2().line(start.x, start.y, end.x, end.y)
        if (line.size > sightRange) {
            return false
        }

        for (i in 0 until line.size) {
            val point = line[i]
            if (gameMap.blockSight(point.x, point.y)) {
                return false
            }
        }

        return true
    }
}