package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class Ai : Component, Pool.Poolable {

    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<Ai>()
    }
}