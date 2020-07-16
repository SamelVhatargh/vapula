package com.github.samelVhatargh.vapula.console.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.systems.FieldOfViewCalculator
import ktx.ashley.get
import ktx.ashley.getSystem

fun removeFog(map: GameMap, player: Entity, engine: Engine) {
    engine.removeSystem(engine.getSystem<FieldOfViewCalculator>())
    val fov = player[FieldOfView.mapper]!!
    fov.visibleTiles.clear()

    for (x in map.tiles.indices) {
        for (y in map.tiles[x].indices) {
            map.tiles[x][y].explored = true
            fov.visibleTiles.add(Position(x, y))
        }
    }
}