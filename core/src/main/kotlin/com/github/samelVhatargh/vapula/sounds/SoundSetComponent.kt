package com.github.samelVhatargh.vapula.sounds

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.sounds.soundEffects.SoundEffectType
import ktx.ashley.mapperFor

/**
 * Represents sounds that entity can produce
 */
class SoundSetComponent : Component, Pool.Poolable {

    var move: SoundEffectType? = null
    var attack: SoundEffectType? = null
    var hit: SoundEffectType? = null
    var death: SoundEffectType? = null

    override fun reset() {
        move = null
        attack = null
        hit = null
        death = null
    }

    companion object {
        val mapper = mapperFor<SoundSetComponent>()
    }
}
