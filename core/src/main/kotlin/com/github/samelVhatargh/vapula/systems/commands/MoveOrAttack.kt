package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.getEntityAtPosition
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.GameMap
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.systems.commands.effects.Effect
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get

class MoveOrAttack : EntitySystem() {
    fun execute(entity: Entity, direction: Direction, gameMap: GameMap) {
        val entityPosition = entity[Position.mapper]!!
        val targetPosition = Position(entityPosition.x + direction.x, entityPosition.y + direction.y)
        val target = engine.getEntityAtPosition(targetPosition, allOf(Stats::class).exclude(Dead::class).get())
        if (target == null) {
            applyEffects(MoveInDirection(engine, entity, direction, gameMap).execute())
            return
        }

        applyEffects(Attack(engine.notifier, entity, target).execute())
    }

    // todo refactor this
    private fun applyEffects(effects: Array<Effect>) {
        effects.forEach {
            val subEffects = it.apply()
            applyEffects(subEffects)
        }
    }
}