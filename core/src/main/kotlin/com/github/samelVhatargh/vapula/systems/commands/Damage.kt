package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.events.EntityDamaged
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.plusAssign

/**
 * Damages entity
 */
class Damage(
    private val engine: Engine,
    private val attacker: Entity,
    private val defender: Entity,
    private val damage: Int
) : Command {

    private val defenderStats = defender[Stats.mapper]!!

    override fun execute() {
        if (defender.has(Invulnerability.mapper)) {
            engine.notifier.notify(EntityDamaged(attacker, defender, 0))
            return
        }

        defenderStats.hp -= damage
        engine.notifier.notify(EntityDamaged(attacker, defender, damage))
        if (!defender.has(Animation.mapper)) {
            defender += Animation(DamageAnimation(defender[Position.mapper]!!))
        }

        if (defenderStats.hp <= 0) {
            Kill(engine, defender).execute()
        }
    }
}