package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.events.EntityDied
import com.github.samelVhatargh.vapula.events.NotifierInterface
import ktx.ashley.get
import ktx.ashley.plusAssign
import ktx.ashley.remove
import ktx.math.vec2

/**
 * Kills entity
 */
class Kill(private val notifier: NotifierInterface, private val entity: Entity) : Command {
    override fun execute(): Boolean {
        val graphics = entity[Graphics.mapper]!!

        entity += Dead()
        entity.remove<Animation>()
        entity.remove<IdleAnimation>()
        graphics.scale = vec2(1f, 1f)
        graphics.layer = Layer.CORPSE

        notifier.notify(EntityDied(entity))

        return false
    }
}