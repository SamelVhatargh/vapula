package com.github.samelVhatargh.vapula.sounds

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.assets.AssetManager
import com.github.samelVhatargh.vapula.assets.MusicAsset
import com.github.samelVhatargh.vapula.assets.get

class MusicSystem(private val assets: AssetManager) : EntitySystem() {

    private var musicStarted = false

    override fun update(deltaTime: Float) {
        if (!musicStarted) {
            val music = assets[MusicAsset.CAVE]
            music.isLooping = true
            music.play()
            musicStarted = true
        }
    }
}