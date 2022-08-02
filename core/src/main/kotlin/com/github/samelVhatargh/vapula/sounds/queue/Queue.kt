package com.github.samelVhatargh.vapula.sounds.queue

import com.github.samelVhatargh.vapula.sounds.SoundEffectType


class Channel {
    private val count: HashMap<SoundEffectType, Int> = hashMapOf()
    private val pan: HashMap<SoundEffectType, Int> = hashMapOf()
    private val volume: HashMap<SoundEffectType, Int> = hashMapOf()

    fun addSound(type: SoundEffectType, volume: Float, pan: Float) {
        val currentCount = count.getOrDefault(type, 0) + 1
        count[type] = currentCount


    }

    fun play() {

    }
}


/**
 * Help to resolve playing same simultaneous sounds
 */
class Queue {

    private val center = Channel()
    private val left = Channel()
    private val right = Channel()


    fun addSound(type: SoundEffectType, volume: Float, pan: Float) {
        if (pan < 0f) {
            left.addSound(type, volume, pan)
            return
        }

        if (pan > 0f) {
            right.addSound(type, volume, pan)
            return
        }

        center.addSound(type, volume, pan)
    }


    fun play() {
        left.play()
        center.play()
        right.play()
    }
}