package com.github.samelVhatargh.vapula.sounds

import com.github.samelVhatargh.vapula.assets.SoundAsset

/**
 * Contains logic of loading specific footstep sound
 */
class StepSound() : SoundEffectType {
    override fun getSoundAsset(): SoundAsset {
        return arrayOf(
            SoundAsset.STEP_1,
            SoundAsset.STEP_2,
            SoundAsset.STEP_3,
            SoundAsset.STEP_4,
            SoundAsset.STEP_5,
            SoundAsset.STEP_6,
            SoundAsset.STEP_7,
            SoundAsset.STEP_8,
            SoundAsset.STEP_9,
            SoundAsset.STEP_10,
        ).random()
    }
}