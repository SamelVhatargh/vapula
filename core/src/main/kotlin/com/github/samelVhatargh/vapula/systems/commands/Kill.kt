package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.events.EntityDied
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.sounds.Creature
import com.github.samelVhatargh.vapula.sounds.DeathSound
import ktx.ashley.*

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

        val creaturePosition = entity[Position.mapper]!!

        entity[SoundSet.mapper]?.death?.let {
            engine.entity {
                with<SoundEffect> {
                    type = it
                    position = creaturePosition
                }
            }
        }

        engine.notifier.notify(EntityDied(entity))
    }
}