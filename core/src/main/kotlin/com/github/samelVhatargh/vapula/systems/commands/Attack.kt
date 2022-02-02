package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.EntityAttacked
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.get

class Attack(
    private val engine: Engine,
    private val attacker: Entity,
    private val defender: Entity
) : Command {

    override fun execute() {
        val attackerStats = attacker[Stats.mapper]!!
        val defenderStats = defender[Stats.mapper]!!

        var hitChance = 65
        hitChance += (attackerStats.dexterity + (attackerStats.perception / 2)) * 5
        hitChance -= (defenderStats.perception + (defenderStats.dexterity / 2)) * 5

        val hit = random.chance(hitChance)

        if (!hit) {
            engine.notifier.notify(EntityAttacked(attacker, defender, true))
            return
        }

        val damage = (1..attackerStats.damageDice).random() + (attackerStats.strength / 2)
        engine.notifier.notify(EntityAttacked(attacker, defender, false))

        Damage(engine.notifier, attacker, defender, damage).execute()
    }
}