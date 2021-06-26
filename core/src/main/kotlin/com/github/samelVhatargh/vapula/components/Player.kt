package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.systems.commands.Command
import ktx.ashley.mapperFor

/**
 * Маркер игрока
 */
class Player : Component, Pool.Poolable {

    var command: Command? = null

    override fun reset() {
        command = null
    }

    companion object {
        val mapper = mapperFor<Player>()
    }
}