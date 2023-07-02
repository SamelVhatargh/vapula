package com.github.samelVhatargh.vapula.game.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.events.EntityDamaged
import com.github.samelVhatargh.vapula.events.NotifierInterface
import ktx.ashley.get
import ktx.ashley.has

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

    override fun execute(): Boolean {
        if (defender.has(Invulnerability.mapper)) {
            notifier.notify(EntityDamaged(attacker, defender, 0))
            return false
        }

        defenderStats.hp -= damage
        notifier.notify(EntityDamaged(attacker, defender, damage))

        if (defenderStats.hp <= 0) {
            return Kill(notifier, defender).execute()
        }

        return false
    }
}