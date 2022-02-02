package com.github.samelVhatargh.vapula.sounds

import com.github.samelVhatargh.vapula.assets.SoundAsset

enum class AttackType {
    RANGE, MELEE, MAGIC
}

class AttackSound(val type: AttackType) : SoundEffectType {
    override fun getSoundAsset(): SoundAsset {
        val assets = when (type) {
            AttackType.RANGE -> arrayOf(
                SoundAsset.RANGE_ATTACK_01
            )
            AttackType.MELEE -> arrayOf(
                SoundAsset.MELEE_ATTACK_01,
                SoundAsset.MELEE_ATTACK_02,
                SoundAsset.MELEE_ATTACK_03,
            )
            AttackType.MAGIC -> arrayOf(
                SoundAsset.MAGIC_ATTACK_01
            )
        }

        return assets.random()
    }
}