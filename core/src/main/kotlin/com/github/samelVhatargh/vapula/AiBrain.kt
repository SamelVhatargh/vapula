package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Bresenham2
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.systems.commands.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.ashley.has

/**
 * Decides what a monster will do
 */
class AiBrain(private val engine: Engine, world: World) {
    private val player = world.player
    private val storey = world.storey
    private val pathFinder = PathFinder(storey, engine)

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
        val monsterStats = entity[Stats.mapper]!!
        val ai = entity[Ai.mapper]!!

        if (entity.has(Dead.mapper)) {
            return DoNothing()
        }

        if (player.has(Dead.mapper)) {
            return wander(entity)
        }

        // start pursuing if player us visible
        val playerIsInLingOfSight = isInLineOfSight(entity, playerPosition)
        if (playerIsInLingOfSight) {
            ai.lastKnownPlayerPosition = playerPosition
            ai.state = State.PURSUE
        }

        // try to heal if possible and needed
        if (monsterStats.healDice > 0) {
            // find all injured hostiles
            val allMonsters = allOf(Stats::class, Ai::class, Position::class).exclude(Player::class, Dead::class).get()
            val injuredMonster = engine.getEntitiesFor(allMonsters).find {
                it[Stats.mapper]!!.hp > 0 && it[Stats.mapper]!!.hp < it[Stats.mapper]!!.maxHp
                        && isInLineOfSight(entity, it[Position.mapper]!!)
            }

            if (injuredMonster !== null) {
                return Heal(engine.notifier, entity, injuredMonster)
            }
        }

        // if player is near - attack!
        if (playerPosition.isNeighbourTo(monsterPosition)) {
            return Attack(engine.notifier, entity, player)
        }

        // if can shoot and in line of sight - attack!
        if (playerIsInLingOfSight && monsterStats.ranged) {
            return Attack(engine.notifier, entity, player)
        }

        val lastKnownPlayerPosition = ai.lastKnownPlayerPosition
        if ((lastKnownPlayerPosition === null || monsterPosition === playerPosition) && ai.state === State.PURSUE) {
            ai.state = State.WANDER
            ai.lastKnownPlayerPosition = null
        }

        if (playerIsInLingOfSight) {  // run to player if he is visible
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

        return MoveInPath(engine, entity, path, storey)
    }

    private fun wander(entity: Entity): Command = MoveInDirection(engine, entity, directions.random(), storey)

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
            if (storey.blockSight(point.x, point.y)) {
                return false
            }
        }

        return true
    }
}