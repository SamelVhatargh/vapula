package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.get
import ktx.math.vec2

/**
 * Тайлы которые видны игроку
 */
class FieldOfView {

    private val visibleTiles = mutableListOf<Vector2>()

    fun update(player: Entity) {
        visibleTiles.clear()
        val visionRadius = 3

        val position = player[Position.mapper]!!

        for (i in (1..(visionRadius * 2 + 1))) {
            for (j in (1..(visionRadius * 2 + 1))) {
                visibleTiles.add(
                    vec2(
                        (position.x - 1 - visionRadius + i).toFloat(),
                        (position.y - 1 - visionRadius + j).toFloat()
                    )
                )
            }
        }

    }

    fun isVisible(position: Vector2): Boolean {
        return visibleTiles.contains(position)
    }
}