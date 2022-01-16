package com.github.samelVhatargh.vapula.sounds

import com.github.samelVhatargh.vapula.assets.SoundAsset

enum class Creature {
    GOBLIN, PLAYER
}

class DeathSound(private val creature: Creature) : SoundEffectType {
    override fun getSoundAsset(): SoundAsset {
        val assets = when (creature) {
            Creature.GOBLIN -> arrayOf(
                SoundAsset.GOBLIN_DEATH_01,
                SoundAsset.GOBLIN_DEATH_02,
            )
            Creature.PLAYER -> arrayOf(
                SoundAsset.PLAYER_DEATH_01,
            )
        }

        return assets.random()
    }
}