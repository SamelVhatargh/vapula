package com.github.samelVhatargh.vapula.graphics

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.mapperFor

/**
 * Represents all variables required to render idle or breathing animation
 */
class IdleAnimationComponent(var length: Float = random.range(1f..3f)) : Component, Pool.Poolable {

    var progress = 0f

    override fun reset() {
        progress = 0f
        length = 1f
    }

    companion object {
        val mapper = mapperFor<IdleAnimationComponent>()
    }
}