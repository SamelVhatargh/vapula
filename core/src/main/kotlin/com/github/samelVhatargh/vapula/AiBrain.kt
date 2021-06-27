package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Bresenham2
import com.github.samelVhatargh.vapula.components.*
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
        val ai = entity[Ai.mapper]!!

        if (player.has(Dead.mapper)) {
            return wander(entity)
        }

        //if player is near - attack!
        if (playerPosition.isNeighbourTo(monsterPosition)) {
            return Attack(engine.notifier, entity, player)
        }

        val lastKnownPlayerPosition = ai.lastKnownPlayerPosition
        if ((lastKnownPlayerPosition === null || monsterPosition === playerPosition) && ai.state === State.PURSUE) {
            ai.state = State.WANDER
            ai.lastKnownPlayerPosition = null
        }

        if (isInLineOfSight(entity, playerPosition)) {  // run to player if he is visible
            ai.lastKnownPlayerPosition = playerPosition
            ai.state = State.PURSUE
            return moveToPlayer(monsterPosition, playerPosition, entity)
        } else if (ai.state === State.PURSUE && lastKnownPlayerPosition !== null) { // or run to where he was last seen
            return moveToPlayer(monsterPosition, lastKnownPlayerPosition, entity)
        }

        return wander(entity)
    }

    private fun moveToPlayer(monsterPosition: Position, playerPosition: Position, entity: Entity): Command {
        val path = pathFinder.findPath(monsterPosition, playerPosition)
        if (path.isEmpty()) {
            return wander(entity)
        }

        return MoveInPath(engine, entity, path, gameMap)
    }

    private fun wander(entity: Entity): Command = MoveInDirection(engine, entity, directions.random(), gameMap)

    /**
     * Check if Entity can see another Entity
     */
    private fun isInLineOfSight(entity: Entity, targetPosition: Position): Boolean {
        val start = entity[Position.mapper]!!
        val sightRange = entity[Stats.mapper]!!.sightRange

        val line = Bresenham2().line(start.x, start.y, targetPosition.x, targetPosition.y)
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