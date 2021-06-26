package com.github.samelVhatargh.vapula.utility

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.log.debug
import ktx.log.logger

/**
 * Ensures that there is only on instance of each [Sprite]
 */
class SpriteCache(private val atlas: TextureAtlas) {

    companion object {
        val log = logger<SpriteCache>()
    }

    private val cache = mutableMapOf<String, Sprite>()

    fun getSprite(name: String): Sprite {
        var sprite = cache[name]
        if (sprite == null) {
            val region = atlas.findRegion(name)
            require(region != null) { "Cant load sprite $name" }

            sprite = Sprite(region).apply {
                setSize(1f, 1f)
            }
            cache[name] = sprite

            log.debug { "sprite $name created" }
        }
        return sprite
    }
}