package com.github.samelVhatargh.vapula.sounds

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.sounds.soundEffects.NoSound
import com.github.samelVhatargh.vapula.sounds.soundEffects.SoundEffectType
import ktx.ashley.mapperFor

class SoundEffectComponent : Component, Pool.Poolable {

    var position: Position = Position()
    var type: SoundEffectType = NoSound()

    override fun reset() {
        position = Position()
        type = NoSound()
    }

    companion object {
        val mapper = mapperFor<SoundEffectComponent>()
    }
}
