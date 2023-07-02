package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Позиция сущности на карте
 */
class PositionComponent(var x: Int = 0, var y: Int = 0, var z: Int = 0) : Component, Pool.Poolable {

    override fun reset() {
        x = 0
        y = 0
        z = 0
    }

    override fun toString(): String {
        return "($x;$y;$z)"
    }

    companion object {
        val mapper = mapperFor<PositionComponent>()
    }

    fun isNeighbourTo(position: PositionComponent): Boolean =
        abs(position.x - x) <= 1 && abs(position.y - y) <= 1 && position.z == z

    /**
     * Returns distance to another position, ignoring storeys
     */
    fun distanceTo(target: PositionComponent): Float {
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

        other as PositionComponent

        if (x != other.x) return false
        if (y != other.y) return false

        return z == other.z
    }

    operator fun plus(direction: Direction): PositionComponent {
        return PositionComponent(x + direction.x, y + direction.y, z)
    }

    fun toVec2(): Vector2 = vec2(x.toFloat(), y.toFloat())

    fun clone(): PositionComponent = PositionComponent(x, y, z)
}