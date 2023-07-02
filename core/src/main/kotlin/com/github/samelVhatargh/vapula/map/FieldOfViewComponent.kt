package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Тайлы которые видны игроку
 */
class FieldOfViewComponent : Component, Pool.Poolable {

    val visibleTiles = mutableListOf<PositionComponent>()

    var seeEverything = false
    var shouldUpdate = true

    fun isVisible(position: PositionComponent): Boolean {
        return seeEverything || visibleTiles.contains(position)
    }

    companion object {
        val mapper = mapperFor<FieldOfViewComponent>()
    }

    override fun reset() {
        shouldUpdate = false
        seeEverything = false
        visibleTiles.clear()
    }
}