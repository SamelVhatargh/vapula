package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.github.samelVhatargh.vapula.components.Invulnerability
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.EntityAttacked
import com.github.samelVhatargh.vapula.events.EntityDamaged
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.get
import ktx.ashley.getSystem
import ktx.ashley.has

class Attack : EntitySystem() {

    fun execute(attacker: Entity, defender: Entity) {
        val attackerStats = attacker[Stats.mapper]!!
        val defenderStats = defender[Stats.mapper]!!

        var hitChance = 65
        hitChance += (attackerStats.dexterity + (attackerStats.perception / 2)) * 5
        hitChance -= (defenderStats.perception + (defenderStats.dexterity / 2)) * 5

        println(hitChance)

        val hit = random.chance(hitChance)

        if (!hit) {
            engine.notifier.notify(EntityAttacked(attacker, defender, true))
            return
        }

        if (defender.has(Invulnerability.mapper)) {
            engine.notifier.notify(EntityAttacked(attacker, defender, false))
            engine.notifier.notify(EntityDamaged(defender, 0))

            return
        }

        val damage = (1..attackerStats.damageDice).random()
        defenderStats.hp -= damage

        engine.notifier.notify(EntityAttacked(attacker, defender, false))
        engine.notifier.notify(EntityDamaged(defender, damage))

        if (defenderStats.hp <= 0) {
            engine.getSystem<Kill>().execute(defender)
        }
    }
}