package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Invulnerability
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.EntityDamaged
import com.github.samelVhatargh.vapula.events.NotifierInterface
import ktx.ashley.get
import ktx.ashley.has

/**
 * Damages entity
 */
class Damage(private val notifier: NotifierInterface, private val entity: Entity, private val damage: Int) : Command {

    private val defenderStats = entity[Stats.mapper]!!

    override fun execute() {
        if (entity.has(Invulnerability.mapper)) {
            notifier.notify(EntityDamaged(entity, 0))
            return
        }

        defenderStats.hp -= damage
        notifier.notify(EntityDamaged(entity, damage))

        if (defenderStats.hp <= 0) {
            Kill(notifier, entity).execute()
        }
    }
}