package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.events.EntityDamaged
import com.github.samelVhatargh.vapula.events.NotifierInterface
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.plusAssign

/**
 * Damages entity
 */
class Damage(
    private val notifier: NotifierInterface,
    private val attacker: Entity,
    private val defender: Entity,
    private val damage: Int
) : Command {

    private val defenderStats = defender[Stats.mapper]!!

    override fun execute() {
        if (defender.has(Invulnerability.mapper)) {
            notifier.notify(EntityDamaged(attacker, defender, 0))
            return
        }

        defenderStats.hp -= damage
        notifier.notify(EntityDamaged(attacker, defender, damage))

        if (defenderStats.hp <= 0) {
            Kill(notifier, defender).execute()
        }
    }
}