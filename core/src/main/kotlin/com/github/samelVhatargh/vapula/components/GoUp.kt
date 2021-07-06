package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Indicates that entity is used for climbing up to the next storey
 */
class GoUp : Component, Pool.Poolable {

    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<GoUp>()
    }
}