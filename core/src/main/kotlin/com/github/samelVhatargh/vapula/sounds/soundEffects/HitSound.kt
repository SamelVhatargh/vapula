package com.github.samelVhatargh.vapula.sounds.soundEffects

import com.github.samelVhatargh.vapula.assets.SoundAsset

enum class HitType {
    SLASH, PIERCE, CRUSH, MAGIC
}

class HitSound(private val type: HitType) : SoundEffectType {
    override fun getSoundAsset(): SoundAsset {
        val assets = when (type) {
            HitType.SLASH -> arrayOf(
                SoundAsset.SWORD_HIT_01,
                SoundAsset.SWORD_HIT_02,
            )
            HitType.CRUSH -> arrayOf(
                SoundAsset.CRUSH_HIT_01,
                SoundAsset.CRUSH_HIT_02,
                SoundAsset.CRUSH_HIT_03,
                SoundAsset.CRUSH_HIT_04,
                SoundAsset.CRUSH_HIT_05,
                SoundAsset.CRUSH_HIT_06,
                SoundAsset.CRUSH_HIT_07,
            )
            HitType.PIERCE -> arrayOf(
                SoundAsset.ARROW_HIT_01,
                SoundAsset.ARROW_HIT_02,
            )
            HitType.MAGIC -> arrayOf(
                SoundAsset.MAGIC_HIT_01,
            )
        }

        return assets.random()
    }
}