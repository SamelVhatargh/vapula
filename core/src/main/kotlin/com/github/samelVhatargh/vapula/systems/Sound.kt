package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.github.samelVhatargh.vapula.components.SoundEffect
import ktx.ashley.allOf
import ktx.ashley.get

class Sound(private val assets: AssetManager) : IteratingSystem(allOf(SoundEffect::class).get()) {


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val soundEffect = entity[SoundEffect.mapper]!!
        assets[soundEffect.type.getSoundAsset().descriptor].play()
        engine.removeEntity(entity)
    }
}