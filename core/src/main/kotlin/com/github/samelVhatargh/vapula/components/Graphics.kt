package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Спрайт для отображения сущности
 */
class Graphics : Component, Pool.Poolable {

    val sprite = Sprite()

    var visible = false

    override fun reset() {
        visible = false
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
    }

    /**
     * Устаналваивает регион из атласа.
     *
     * Должен быть указан при инициализации компонента
     */
    fun setSpriteRegion(region: TextureRegion) {
        sprite.setRegion(region)
        sprite.setSize(1f, 1f)
    }

    companion object {
        val mapper = mapperFor<Graphics>()
    }
}