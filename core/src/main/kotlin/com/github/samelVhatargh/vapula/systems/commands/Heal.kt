package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.events.NotifierInterface
import com.github.samelVhatargh.vapula.systems.commands.effects.Effect
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.get
import com.github.samelVhatargh.vapula.systems.commands.effects.Heal as HealEffect

class Heal(val notifier: NotifierInterface, private val healer: Entity, private val injuredMonster: Entity) : Command {
    override fun execute(): Array<Effect> {
        val healDice = healer[Stats.mapper]!!.healDice
        return arrayOf(HealEffect(notifier, healer, injuredMonster, random.dice(1, healDice)))
    }

}
