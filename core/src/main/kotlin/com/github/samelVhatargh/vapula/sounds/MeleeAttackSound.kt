package com.github.samelVhatargh.vapula.sounds

import com.github.samelVhatargh.vapula.assets.SoundAsset

class MeleeAttackSound : SoundEffectType {
    override fun getSoundAsset(): SoundAsset {
        return arrayOf(
            SoundAsset.MELEE_ATTACK_01,
            SoundAsset.MELEE_ATTACK_02,
            SoundAsset.MELEE_ATTACK_03,
        ).random()
    }
}