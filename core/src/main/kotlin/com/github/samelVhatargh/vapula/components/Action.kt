package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.systems.commands.Command
import com.github.samelVhatargh.vapula.systems.commands.DoNothing
import ktx.ashley.mapperFor

/**
 * Represents a [command][com.github.samelVhatargh.vapula.systems.commands.Command] that an entity should execute
 */
class Action(var command: Command) : Component, Pool.Poolable {

    override fun reset() {
        command = DoNothing()
    }

    companion object {
        val mapper = mapperFor<Action>()
    }
}