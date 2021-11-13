package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.events.EntityAttacked
import com.github.samelVhatargh.vapula.events.Notifier
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger

class Attack(
    private val notifier: Notifier,
    private val attacker: Entity,
    private val defender: Entity,
    private val entityFactory: Factory
) : Command {

    override fun execute() {
        val attackerStats = attacker[Stats.mapper]!!
        val defenderStats = defender[Stats.mapper]!!
        val attackerPosition = attacker[Position.mapper]!!
        val defenderPosition = defender[Position.mapper]!!

        var hitChance = 65
        hitChance += (attackerStats.dexterity + (attackerStats.perception / 2)) * 5
        hitChance -= (defenderStats.perception + (defenderStats.dexterity / 2)) * 5

        val hit = random.chance(hitChance)
        attacker.add(Animation(AttackAnimation(attackerPosition, defenderPosition)))

        if (attackerStats.ranged) {
            var targetPosition = Position(defenderPosition.x, defenderPosition.y, defenderPosition.z)
            if (!hit) {
                targetPosition += Direction.values().random()
            }
            val arrow = entityFactory.createArrow(attackerPosition, targetPosition)
            arrow.add(Animation(ProjectileAnimation(attackerPosition, targetPosition)))
        }

        if (!hit) {
            notifier.notify(EntityAttacked(attacker, defender, true))
            return
        }

        val damage = (1..attackerStats.damageDice).random() + (attackerStats.strength / 2)
        notifier.notify(EntityAttacked(attacker, defender, false))

        Damage(notifier, defender, damage).execute()
    }
}