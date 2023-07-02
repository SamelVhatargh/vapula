package com.github.samelVhatargh.vapula.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Маркер игрока
 */
class PlayerComponent : Component, Pool.Poolable {

    override fun reset() {
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}