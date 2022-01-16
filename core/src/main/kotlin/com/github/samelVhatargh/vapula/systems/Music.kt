package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.assets.AssetManager
import com.github.samelVhatargh.vapula.assets.MusicAsset
import com.github.samelVhatargh.vapula.assets.get

class Music(private val assets: AssetManager) : EntitySystem() {

    var musicStarted = false

    override fun update(deltaTime: Float) {
        if (!musicStarted) {
            val music = assets[MusicAsset.CAVE]
            music.isLooping = true
            music.play()
            musicStarted = true
        }
    }
}