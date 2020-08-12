package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.github.samelVhatargh.vapula.components.Invulnerability
import com.github.samelVhatargh.vapula.components.Name
import com.github.samelVhatargh.vapula.components.Stats
import ktx.ashley.get
import ktx.ashley.getSystem
import ktx.ashley.has
import ktx.log.debug

class Attack : EntitySystem() {

    fun execute(attacker: Entity, defender: Entity) {
        val attackerStats = attacker[Stats.mapper]!!
        val defenderStats = defender[Stats.mapper]!!

        if (defender.has(Invulnerability.mapper)) {
            debug { "${attacker[Name.mapper]!!.name} attacks ${defender[Name.mapper]!!.name} but ${defender[Name.mapper]!!.name} is invulnerable" }
            return
        }

        val damage = (1..attackerStats.damageDice).random()
        defenderStats.hp -= damage
        debug { "${attacker[Name.mapper]!!.name} attacks ${defender[Name.mapper]!!.name} for $damage damage" }
        debug { "${defender[Name.mapper]!!.name} has ${defenderStats.hp} hp left" }

        if (defenderStats.hp < 0) {
            engine.getSystem<Kill>().execute(defender)
        }
    }
}