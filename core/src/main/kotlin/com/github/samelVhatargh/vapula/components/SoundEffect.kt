package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.sounds.NoSound
import com.github.samelVhatargh.vapula.sounds.SoundEffectType
import ktx.ashley.mapperFor

class SoundEffect : Component, Pool.Poolable {

    var position: Position = Position()
    var type: SoundEffectType = NoSound()

    override fun reset() {
        position = Position()
        type = NoSound()
    }

    companion object {
        val mapper = mapperFor<SoundEffect>()
    }
}
