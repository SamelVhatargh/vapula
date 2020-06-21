package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Bresenham2
import com.badlogic.gdx.math.Vector2
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.get
import ktx.math.vec2

class FieldOfViewCalculator(private val player: Entity, map: Entity) : EntitySystem() {

    private val gameMap = map[GameMap.mapper]!!

    override fun update(deltaTime: Float) {
        player[FieldOfView.mapper]?.let { fov ->
            if (!fov.shouldUpdate) return
            fov.reset()

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
                    fov.visibleTiles.add(vec2(point.x.toFloat(), point.y.toFloat()))
                    if (gameMap.blockSight(point.x, point.y)) {
                        break
                    }
                }
            }
        }
    }
}