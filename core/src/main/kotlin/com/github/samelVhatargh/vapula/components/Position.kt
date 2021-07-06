package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.map.Direction
import ktx.ashley.mapperFor
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Позиция сущности на карте
 */
class Position(var x: Int = 0, var y: Int = 0, var z: Int = 0) : Component, Pool.Poolable {

    override fun reset() {
        x = 0
        y = 0
        z = 0
    }

    override fun toString(): String {
        return "($x;$y;$z)"
    }

    companion object {
        val mapper = mapperFor<Position>()
    }

    fun isNeighbourTo(position: Position): Boolean =
        abs(position.x - x) <= 1 && abs(position.y - y) <= 1 && position.z == z

    /**
     * Returns distance to another position, ignoring storeys
     */
    fun distanceTo(target: Position): Float {
        val dx = (target.x - x).toDouble()
        val dy = (target.y - y).toDouble()
        return sqrt(dx * dx + dy * dy).toFloat()
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    operator fun plus(direction: Direction): Position {
        return Position(x + direction.x, y + direction.y, z)
    }
}