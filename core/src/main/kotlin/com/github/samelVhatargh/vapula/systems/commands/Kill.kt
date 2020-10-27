package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Layer
import com.github.samelVhatargh.vapula.events.EntityDied
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.get
import ktx.ashley.plusAssign

class Kill(private val spriteAtlas: TextureAtlas) : EntitySystem() {
    fun execute(entity: Entity) {
        val graphics = entity[Graphics.mapper]!!

        entity += Dead()
        graphics.setSpriteRegion(spriteAtlas.findRegion("${graphics.spriteName}Dead"))
        graphics.layer = Layer.CORPSE

        engine.notifier.notify(EntityDied(entity))
    }
}