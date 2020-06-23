package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class Name(var name: String = "") : Component, Pool.Poolable {

    override fun reset() {
        name = ""
    }

    companion object {
        val mapper = mapperFor<Name>()
    }
}