package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Позиция сущности на карте
 */
class Position(var x: Int = 0, var y: Int = 0) : Component, Pool.Poolable {

    override fun reset() {
        x = 0
        y = 0
    }

    override fun toString(): String {
        return "($x;$y)"
    }

    companion object {
        val mapper = mapperFor<Position>()
    }
}
