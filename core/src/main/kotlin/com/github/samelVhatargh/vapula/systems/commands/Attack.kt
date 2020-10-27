package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.github.samelVhatargh.vapula.components.Invulnerability
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.EntityAttacked
import com.github.samelVhatargh.vapula.events.EntityDamaged
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.get
import ktx.ashley.getSystem
import ktx.ashley.has

class Attack : EntitySystem() {

    fun execute(attacker: Entity, defender: Entity) {
        val attackerStats = attacker[Stats.mapper]!!
        val defenderStats = defender[Stats.mapper]!!

        if (defender.has(Invulnerability.mapper)) {
            engine.notifier.notify(EntityAttacked(attacker, defender, false))
            engine.notifier.notify(EntityDamaged(defender, 0))

            return
        }

        val damage = (1..attackerStats.damageDice).random()
        defenderStats.hp -= damage

        engine.notifier.notify(EntityAttacked(attacker, defender, false))
        engine.notifier.notify(EntityDamaged(defender, damage))

        if (defenderStats.hp < 0) {
            engine.getSystem<Kill>().execute(defender)
        }
    }
}