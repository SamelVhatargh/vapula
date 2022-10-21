package com.github.samelVhatargh.vapula.sounds.queue

import com.badlogic.gdx.assets.AssetManager
import com.github.samelVhatargh.vapula.sounds.SoundEffectType
import ktx.math.random
import kotlin.math.absoluteValue

/**
 * Describes parameters of playable sound effect
 */
private data class Sound(val type: SoundEffectType, val volume: Float, val pan: Float)

/**
 * Channel stores queue of sounds
 */
private class Channel {
    private val sounds: HashMap<SoundEffectType, MutableList<Sound>> = linkedMapOf()

    fun addSound(sound: Sound) {
        val type = sound.type
        val stored = sounds[type]
        if (stored == null) {
            sounds[type] = mutableListOf(sound)
            return
        }
        stored.add(sound)
    }

    /**
     * Returns array of sound effect to play
     *
     * Duplicated sounds effects are considered as one sound effect with increased volume.
     */
    fun getSounds(): Array<Sound> {
        return Array(sounds.size) {
            val soundEffectType = sounds.keys.first()
            val parameters = sounds[soundEffectType]!!
            val soundCount = parameters.size

            var loudestSound = parameters.first()

            //todo parameters.reduce
            parameters.forEach {
                if (it.volume > loudestSound.volume) {
                    loudestSound = it
                } else {
                    if (it.volume == loudestSound.volume) {
                        if (it.pan.absoluteValue < loudestSound.pan.absoluteValue) {
                            loudestSound = it
                        }
                    }
                }
            }

            sounds.remove(soundEffectType)
            Sound(soundEffectType, loudestSound.volume + (soundCount - 1) * .05f, loudestSound.pan)
        }
    }
}


/**
 * Help to resolve playing same simultaneous sounds
 */
class Queue(private val assets: AssetManager) {

    private val center = Channel()
    private val left = Channel()
    private val right = Channel()


    fun addSound(type: SoundEffectType, volume: Float, pan: Float) {
        if (pan < 0f) {
            left.addSound(Sound(type, volume, pan))
            return
        }

        if (pan > 0f) {
            right.addSound(Sound(type, volume, pan))
            return
        }

        center.addSound(Sound(type, volume, pan))
    }


    fun play() {
        val sounds = left.getSounds() + center.getSounds() + right.getSounds()
        if (sounds.isNotEmpty()) {
            sounds.forEach { playSound(it) }
        }
    }

    private fun playSound(playableSound: Sound) {
        val sound = assets[playableSound.type.getSoundAsset().descriptor]
        val id = sound.play()
        sound.setPitch(id, (.925f..1.075f).random())
        sound.setPan(id, playableSound.pan, playableSound.volume)
    }
}