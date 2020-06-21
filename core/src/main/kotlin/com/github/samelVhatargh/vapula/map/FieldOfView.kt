package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Bresenham2
import com.badlogic.gdx.math.Vector2
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.get
import ktx.math.vec2

/**
 * Тайлы которые видны игроку
 */
class FieldOfView(val map: GameMap) {

    private val visibleTiles = mutableListOf<Vector2>()

    fun update(player: Entity) {
        visibleTiles.clear()
        val visionRadius = 3

        val origin = player[Position.mapper]!!
        val edges = mutableListOf<Vector2>()

        //Находим крайние клетки
        val maxRange = visionRadius * 2 + 1
        for (i in (1..maxRange)) {
            for (j in (1..maxRange)) {
                if (i != 1 && j != 1 && i != maxRange && j != maxRange) {
                    continue
                }
                edges.add(
                    Vector2(
                        (origin.x - 1 - visionRadius + i).toFloat(),
                        (origin.y - 1 - visionRadius + j).toFloat()
                    )
                )
            }
        }

        ///Простейший ray casting алгоритм
        edges.forEach { edge ->
            val line = Bresenham2().line(origin.x, origin.y, edge.x.toInt(), edge.y.toInt())
            for (i in 0 until line.size) {
                val point = line[i]
                visibleTiles.add(vec2(point.x.toFloat(), point.y.toFloat()))
                if (map.blockSight(point.x, point.y)) {
                    break
                }
            }
        }

    }

    fun isVisible(position: Vector2): Boolean {
        return visibleTiles.contains(position)
    }
}