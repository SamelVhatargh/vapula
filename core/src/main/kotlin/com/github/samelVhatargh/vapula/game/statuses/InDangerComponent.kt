package com.github.samelVhatargh.vapula.game.statuses

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class InDangerComponent : Component, Pool.Poolable {

    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<InDangerComponent>()
    }
}