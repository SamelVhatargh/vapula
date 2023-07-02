package com.github.samelVhatargh.vapula.sounds

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.sounds.soundEffects.NoSound
import com.github.samelVhatargh.vapula.sounds.soundEffects.SoundEffectType
import ktx.ashley.mapperFor

class SoundEffectComponent : Component, Pool.Poolable {

    var position: PositionComponent = PositionComponent()
    var type: SoundEffectType = NoSound()

    override fun reset() {
        position = PositionComponent()
        type = NoSound()
    }

    companion object {
        val mapper = mapperFor<SoundEffectComponent>()
    }
}
