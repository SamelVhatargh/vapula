package com.github.samelVhatargh.vapula.graphics

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.map.Direction
import ktx.ashley.mapperFor
import ktx.math.vec2

/**
 * Спрайт для отображения сущности
 */
class GraphicsComponent : Component, Pool.Poolable, Comparable<GraphicsComponent> {

    var spriteName = ""
    var layer = Layer.CREATURE
    var position: Vector2? = null
    var rotation: Float? = null
    var direction: Direction = Direction.WEST
    var scale: Vector2 = vec2(1f, 1f)

    override fun reset() {
        spriteName = ""
        layer = Layer.CREATURE
        position = null
        rotation = null
        direction = Direction.WEST
        scale = vec2(1f, 1f)
    }

    companion object {
        val mapper = mapperFor<GraphicsComponent>()
    }

    override fun compareTo(other: GraphicsComponent): Int = layer.compareTo(other.layer)
}