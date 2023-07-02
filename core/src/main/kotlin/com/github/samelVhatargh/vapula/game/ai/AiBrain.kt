package com.github.samelVhatargh.vapula.game.ai

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Bresenham2
import com.github.samelVhatargh.vapula.game.World
import com.github.samelVhatargh.vapula.game.PlayerComponent
import com.github.samelVhatargh.vapula.game.commands.*
import com.github.samelVhatargh.vapula.game.stats.StatsComponent
import com.github.samelVhatargh.vapula.game.statuses.DeadComponent
import com.github.samelVhatargh.vapula.game.statuses.InDangerComponent
import com.github.samelVhatargh.vapula.map.PathFinder
import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.*

/**
 * Decides what a monster will do
 */
class AiBrain(private val engine: Engine, private val world: World) {
    private val player = world.player
    private val pathFinders = Array(world.stories.size) { z ->
        PathFinder(world.stories[z], engine)
    }

    fun getCommand(entity: Entity): Command {
        val playerPosition = player[PositionComponent.mapper]!!
        val monsterPosition = entity[PositionComponent.mapper]!!
        val monsterStats = entity[StatsComponent.mapper]!!
        val ai = entity[AiComponent.mapper]!!

        if (entity.has(DeadComponent.mapper)) {
            return DoNothing()
        }

        if (player.has(DeadComponent.mapper)) {
            return Wander(engine, entity, world)
        }

        // start pursuing if player us visible
        val playerIsInLingOfSight = isInLineOfSight(entity, player)
        if (playerIsInLingOfSight) {
            ai.lastKnownPlayerPosition = playerPosition
            ai.state = State.PURSUE
        }

        // try to heal if possible and needed
        if (monsterStats.healDice > 0) {
            // find all injured hostiles
            val allMonsters = allOf(StatsComponent::class, AiComponent::class, PositionComponent::class).exclude(PlayerComponent::class, DeadComponent::class).get()
            val injuredMonster = engine.getEntitiesFor(allMonsters).find {
                it[StatsComponent.mapper]!!.hp > 0 && it[StatsComponent.mapper]!!.hp < it[StatsComponent.mapper]!!.maxHp
                        && isInLineOfSight(entity, it)
            }

            if (injuredMonster !== null) {
                return Heal(engine.notifier, entity, injuredMonster)
            }
        }

        // if player is near - attack!
        if (playerPosition.isNeighbourTo(monsterPosition)) {
            return Attack(engine, entity, player)
        }

        // if entity can shoot and in line of sight - attack!
        if (playerIsInLingOfSight && monsterStats.ranged) {
            return Attack(engine, entity, player)
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

    private fun moveToPlayer(monsterPosition: PositionComponent, playerPosition: PositionComponent, entity: Entity): Command {
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
    private fun isInLineOfSight(entity: Entity, target: Entity): Boolean {
        val start = entity[PositionComponent.mapper]!!
        val sightRange = entity[StatsComponent.mapper]!!.sightRange
        val targetPosition = target[PositionComponent.mapper]!!

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

        if (target.has(PlayerComponent.mapper)) {
            target += InDangerComponent()
            target.remove<ActionComponent>()
        }

        return true
    }
}