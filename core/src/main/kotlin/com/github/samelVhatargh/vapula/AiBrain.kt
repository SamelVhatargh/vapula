package com.github.samelVhatargh.vapula

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Bresenham2
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.systems.commands.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.ashley.has

/**
 * Decides what a monster will do
 */
class AiBrain(private val engine: Engine, private val world: World) {
    private val player = world.player
    private val pathFinders = Array(world.stories.size) { z ->
        PathFinder(world.stories[z], engine)
    }

    fun getCommand(entity: Entity): Command {
        val playerPosition = player[Position.mapper]!!
        val monsterPosition = entity[Position.mapper]!!
        val monsterStats = entity[Stats.mapper]!!
        val ai = entity[Ai.mapper]!!

        if (entity.has(Dead.mapper)) {
            return DoNothing()
        }

        if (player.has(Dead.mapper)) {
            return Wander(engine, entity, world)
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
            return Attack(engine.notifier, entity, player, world.entityFactory)
        }

        // if can shoot and in line of sight - attack!
        if (playerIsInLingOfSight && monsterStats.ranged) {
            return Attack(engine.notifier, entity, player, world.entityFactory)
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

        return Wander(engine, entity, world)
    }

    private fun moveToPlayer(monsterPosition: Position, playerPosition: Position, entity: Entity): Command {
        if (monsterPosition.z != playerPosition.z) {
            return Wander(engine, entity, world)
        }

        val path = pathFinders[monsterPosition.z].findPath(monsterPosition, playerPosition)
        if (path.isEmpty()) {
            return Wander(engine, entity, world)
        }

        return MoveInPath(engine, entity, path, world.storey)
    }

    /**
     * Check if Entity can see another Entity
     */
    private fun isInLineOfSight(entity: Entity, targetPosition: Position): Boolean {
        val start = entity[Position.mapper]!!
        val sightRange = entity[Stats.mapper]!!.sightRange

        if (start.z != targetPosition.z) {
            return false
        }

        val line = Bresenham2().line(start.x, start.y, targetPosition.x, targetPosition.y)
        if (line.size > sightRange) {
            return false
        }

        for (i in 0 until line.size) {
            val point = line[i]
            if (world.storey.blockSight(point.x, point.y)) {
                return false
            }
        }

        return true
    }
}