package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.Animation
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Layer
import com.github.samelVhatargh.vapula.events.EntityDied
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.get
import ktx.ashley.plusAssign
import ktx.ashley.remove

/**
 * Kills entity
 */
class Kill(private val engine: Engine, private val entity: Entity) : Command {
    override fun execute() {
        val graphics = entity[Graphics.mapper]!!

        entity += Dead()
        entity.remove<Animation>()
        graphics.spriteName = "${graphics.spriteName}Dead"
        graphics.layer = Layer.CORPSE

        engine.notifier.notify(EntityDied(entity))
    }
}