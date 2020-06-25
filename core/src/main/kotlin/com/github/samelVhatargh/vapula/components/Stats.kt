package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Способности всех существ, которые могут драться
 */
class Stats : Component, Pool.Poolable {

    var maxHp = 0
    var hp = 0
    var damageDice = 0

    override fun reset() {
        maxHp = 0
        hp = 0
        damageDice = 0
    }

    companion object {
        val mapper = mapperFor<Stats>()
    }
}