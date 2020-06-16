package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2

const val DIRECTION_UP = 1f
const val DIRECTION_DOWN = -1f
const val DIRECTION_LEFT = -1f
const val DIRECTION_RIGHT = 1f
const val DIRECTION_NONE = 0f

/**
 * Компонент указывает на то, что сущности необходимо передвинутся в указанном направлении
 */
class MoveDirection : Component, Pool.Poolable {
    val direction = vec2()

    override fun reset() {
        direction.set(0f, 0f)
    }

    companion object {
        val mapper = mapperFor<MoveDirection>()
    }
}