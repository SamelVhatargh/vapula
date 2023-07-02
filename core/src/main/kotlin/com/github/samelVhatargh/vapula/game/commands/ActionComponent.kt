package com.github.samelVhatargh.vapula.game.commands

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Represents a [command][com.github.samelVhatargh.vapula.game.commands.Command] that an entity should execute
 */
class ActionComponent(var command: Command) : Component, Pool.Poolable {

    override fun reset() {
        command = DoNothing()
    }

    companion object {
        val mapper = mapperFor<ActionComponent>()
    }
}