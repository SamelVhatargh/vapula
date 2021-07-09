package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2

enum class AnimationType {
    NONE, WALK
}

class Animation(var start: Position, var end: Position, var type: AnimationType) : Component, Pool.Poolable {

    var vector = vec2(start.x.toFloat(), start.y.toFloat())
    var progress = 0f

    override fun reset() {
        start = Position(0, 0)
        end = Position(0, 0)
        type = AnimationType.NONE
        vector = vec2(0f, 0f)
        progress = 0f
    }

    companion object {
        val mapper = mapperFor<Animation>()
    }
}