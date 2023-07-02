package com.github.samelVhatargh.vapula.game.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.game.stats.StatsComponent
import com.github.samelVhatargh.vapula.events.EntityHealed
import com.github.samelVhatargh.vapula.events.NotifierInterface
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.get

class Heal(val notifier: NotifierInterface, private val healer: Entity, private val injuredMonster: Entity) : Command {
    override fun execute(): Boolean {
        val healDice = healer[StatsComponent.mapper]!!.healDice
        val healedAmount = random.dice(1, healDice)
        injuredMonster[StatsComponent.mapper]!!.hp += healedAmount
        notifier.notify(EntityHealed(healer, injuredMonster, healedAmount))

        return false
    }
}
