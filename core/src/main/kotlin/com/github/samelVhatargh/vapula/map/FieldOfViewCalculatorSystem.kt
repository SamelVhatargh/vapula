package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Bresenham2
import com.badlogic.gdx.math.Vector2
import com.github.samelVhatargh.vapula.game.World
import com.github.samelVhatargh.vapula.game.stats.StatsComponent
import ktx.ashley.get

class FieldOfViewCalculatorSystem(private val world: World) : EntitySystem() {
    private val player = world.player

    override fun update(deltaTime: Float) {
        player[FieldOfViewComponent.mapper]?.let { fov ->
            if (!fov.shouldUpdate) return
            fov.reset()

            val visionRadius = player[StatsComponent.mapper]!!.sightRange

            val origin = player[PositionComponent.mapper]!!
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
                    fov.visibleTiles.add(PositionComponent(point.x, point.y, world.storey.z))
                    if (world.storey.blockSight(point.x, point.y)) {
                        break
                    }
                }
            }
        }
    }
}