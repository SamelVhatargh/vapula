package com.github.samelVhatargh.vapula.sounds.queue

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.github.samelVhatargh.vapula.sounds.SoundEffectType
import ktx.math.random
import kotlin.math.absoluteValue

private data class SoundParameters(val volume: Float, val pan: Float)

private data class PlayableSound(val type: SoundEffectType, val parameters: SoundParameters)

private class Channel {
    private val sounds: HashMap<SoundEffectType, MutableList<SoundParameters>> = linkedMapOf()

    fun addSound(type: SoundEffectType, parameters: SoundParameters) {
        val stored = sounds[type]
        if (stored == null) {
            sounds[type] = mutableListOf(parameters)
            return
        }
        stored.add(parameters)
    }

    fun getPlSounds(): Array<PlayableSound> {
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
            PlayableSound(
                soundEffectType,
                SoundParameters(loudestSound.volume + (soundCount - 1) * .05f, loudestSound.pan)
            )
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
            left.addSound(type, SoundParameters(volume, pan))
            return
        }

        if (pan > 0f) {
            right.addSound(type, SoundParameters(volume, pan))
            return
        }

        center.addSound(type, SoundParameters(volume, pan))
    }


    fun play() {
        val sounds = /*left.getSounds() + center.getSounds() + */right.getPlSounds()
        if (sounds.isNotEmpty()) {
            sounds.forEach { playSound(it) }
        }
    }

    private fun playSound(playableSound: PlayableSound) {
        println(playableSound.type.javaClass.toString() + playableSound.parameters.pan)
        val sound = assets[playableSound.type.getSoundAsset().descriptor]
        val id = sound.play()
        sound.setPitch(id, (.925f..1.075f).random())
        sound.setPan(id, playableSound.parameters.pan, playableSound.parameters.volume)
    }
}