package com.github.samelVhatargh.vapula.systems.commands.effects

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.EntityHealed
import com.github.samelVhatargh.vapula.events.NotifierInterface
import ktx.ashley.get

class Heal(
    val notifier: NotifierInterface,
    private val healer: Entity,
    private val entity: Entity,
    private val damage: Int
) : Effect {
    override fun apply(): Array<Effect> {
        entity[Stats.mapper]!!.hp += damage
        notifier.notify(EntityHealed(healer, entity, damage))
        return emptyArray()
    }
}