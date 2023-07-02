package com.github.samelVhatargh.vapula.debug.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.map.FieldOfViewComponent
import com.github.samelVhatargh.vapula.map.FieldOfViewCalculatorSystem
import ktx.ashley.get
import ktx.ashley.getSystem

fun removeFog(player: Entity, engine: Engine) {
    engine.removeSystem(engine.getSystem<FieldOfViewCalculatorSystem>())
    val fov = player[FieldOfViewComponent.mapper]!!
    fov.visibleTiles.clear()
    fov.seeEverything = true
}