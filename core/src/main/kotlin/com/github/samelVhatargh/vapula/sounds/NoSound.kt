package com.github.samelVhatargh.vapula.sounds

import com.github.samelVhatargh.vapula.assets.SoundAsset

/**
 * No sound, used as dummy
 */
class NoSound: SoundEffectType {
    override fun getSoundAsset(): SoundAsset = SoundAsset.EMPTY
}