package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Name
import ktx.ashley.get
import ktx.ashley.plusAssign
import ktx.log.debug

class Kill(private val spriteAtlas: TextureAtlas) : EntitySystem() {
    fun execute(entity: Entity) {
        val graphics = entity[Graphics.mapper]!!

        entity += Dead()
        graphics.setSpriteRegion(spriteAtlas.findRegion("${graphics.spriteName}Dead"))

        debug { "${entity[Name.mapper]?.name} is dead" }
    }
}