package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Тайлы которые видны игроку
 */
class FieldOfView : Component, Pool.Poolable {

    val visibleTiles = mutableListOf<Vector2>()

    var shouldUpdate = true

    fun isVisible(position: Vector2): Boolean {
        return visibleTiles.contains(position)
    }

    companion object {
        val mapper = mapperFor<FieldOfView>()
    }

    override fun reset() {
        shouldUpdate = false
        visibleTiles.clear()
    }
}