package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class VisibleIfExploredAndOutOfFieldOfViewComponent : Component, Pool.Poolable {

    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<VisibleIfExploredAndOutOfFieldOfViewComponent>()
    }
}