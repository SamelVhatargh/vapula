package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.map.PositionComponent
import ktx.ashley.mapperFor

enum class State {
    WANDER, PURSUE
}

class Ai : Component, Pool.Poolable {

    var state = State.WANDER
    var lastKnownPlayerPosition: PositionComponent? = null

    override fun reset() {
        state = State.WANDER
        lastKnownPlayerPosition = null
    }

    companion object {
        val mapper = mapperFor<Ai>()
    }
}