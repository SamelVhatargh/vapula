package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.AiBrain
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.components.Ai
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.plusAssign

class Ai(private val aiBrain: AiBrain) :
    IteratingSystem(allOf(Ai::class, Name::class, Position::class).exclude(Dead::class).get()) {

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        setProcessing(false)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val command = aiBrain.getCommand(entity)
        entity += Action(command)
    }

}