package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class Layer {
    FLOOR, CORPSE, CREATURE
}

/**
 * Спрайт для отображения сущности
 */
class Graphics : Component, Pool.Poolable, Comparable<Graphics> {

    var spriteName = ""
    var layer = Layer.CREATURE
    var position: Vector2? = null
    var rotation: Float? = null

    override fun reset() {
        spriteName = ""
        layer = Layer.CREATURE
        position = null
        rotation = null
    }

    companion object {
        val mapper = mapperFor<Graphics>()
    }

    override fun compareTo(other: Graphics): Int = layer.compareTo(other.layer)
}