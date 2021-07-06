package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.GoDown
import com.github.samelVhatargh.vapula.components.GoUp
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.allOf
import ktx.ashley.get

abstract class UseStairs(private val engine: Engine, private val world: World, private val entity: Entity) : Command {
    fun useStairs(zDelta: Int, endpointFamily: Family) {
        val newZ = world.storey.z + zDelta
        val stairs = engine.getEntitiesFor(endpointFamily).find {
            it[Position.mapper]!!.z == newZ
        }
        if (stairs != null) {
            val stairsPosition = stairs[Position.mapper]!!
            ChangePosition(entity, stairsPosition).execute()
            world.changeStory(newZ)
        }
    }
}


class GoUpstairs(engine: Engine, world: World, entity: Entity) : UseStairs(engine, world, entity) {
    override fun execute() {
        useStairs(1, allOf(GoDown::class).get())
    }
}

class GoDownstairs(engine: Engine, world: World, entity: Entity) : UseStairs(engine, world, entity) {
    override fun execute() {
        useStairs(-1, allOf(GoUp::class).get())
    }
}