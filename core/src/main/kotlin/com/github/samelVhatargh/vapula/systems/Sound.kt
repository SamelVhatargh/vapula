package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.github.samelVhatargh.vapula.components.SoundEffect
import ktx.ashley.allOf
import ktx.ashley.get

class Sound : IteratingSystem(allOf(SoundEffect::class).get()) {


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val soundEffect = entity[SoundEffect.mapper]!!

        val asset = soundEffect.type.getSoundAsset()
        val sound = Gdx.audio.newSound(Gdx.files.internal(asset.fileName))


        sound.play()

        engine.removeEntity(entity)
    }
}