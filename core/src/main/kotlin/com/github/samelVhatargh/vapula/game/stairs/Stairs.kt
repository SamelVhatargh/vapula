package com.github.samelVhatargh.vapula.game.stairs

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.github.samelVhatargh.vapula.game.World
import com.github.samelVhatargh.vapula.game.commands.ChangePosition
import com.github.samelVhatargh.vapula.game.commands.Command
import com.github.samelVhatargh.vapula.map.PositionComponent
import ktx.ashley.allOf
import ktx.ashley.get

abstract class UseStairs(private val engine: Engine, private val world: World, private val entity: Entity) : Command {
    fun useStairs(zDelta: Int, endpointFamily: Family) {
        val newZ = world.storey.z + zDelta
        val stairs = engine.getEntitiesFor(endpointFamily).find {
            it[PositionComponent.mapper]!!.z == newZ
        }
        if (stairs != null) {
            val stairsPosition = stairs[PositionComponent.mapper]!!
            ChangePosition(entity, stairsPosition).execute()
            world.changeStory(newZ)
        }
    }
}


class GoUpstairs(engine: Engine, world: World, entity: Entity) : UseStairs(engine, world, entity) {
    override fun execute(): Boolean {
        useStairs(1, allOf(GoDown::class).get())

        return false
    }
}

class GoDownstairs(engine: Engine, world: World, entity: Entity) : UseStairs(engine, world, entity) {
    override fun execute(): Boolean {
        useStairs(-1, allOf(GoUp::class).get())

        return false
    }
}