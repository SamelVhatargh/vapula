package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.mapperFor
import kotlin.math.floor
import kotlin.math.min

const val MAX_LEVEL = 10

/**
 * Способности всех существ, которые могут драться
 */
class Stats : Component, Pool.Poolable {

    var level = 0
    var baseHp = 0
    val maxHp: Int
        get() {
            var result = baseHp + (constitution / 2) * level
            for (i in 1..level) {
                result += min(hpArray[i - 1], constitution)
            }
            return result
        }
    var hp = 0
        set(value) {
            if (value > maxHp) {
                field = maxHp
                return
            }
            field = value
        }
    var damageDice = 0
    var healDice = 0
    var ranged = false

    val sightRange: Int
        get() = floor(perception / 3.5).toInt() + 3

    var strength = 0
    var dexterity = 0
    var perception = 0
    var constitution = 0
    var intellegence = 0
    var wisdom = 0
    var charisma = 0

    private var hpArray: IntArray = IntArray(MAX_LEVEL) { 0 }

    override fun reset() {
        level = 0
        hp = 0
        baseHp = 0
        damageDice = 0
        healDice = 0
        ranged = false

        strength = 0
        dexterity = 0
        perception = 0
        constitution = 0
        intellegence = 0
        wisdom = 0
        charisma = 0

        hpArray = IntArray(MAX_LEVEL) { 0 }
    }

    fun generateHp(baseHp: Int, vararg fixedHpArray: Int) {
        this.baseHp = baseHp

        var count = 0
        for (fixedHp in fixedHpArray) {
            hpArray[count] = fixedHp
            count++
        }

        for (level in count until MAX_LEVEL) {
            hpArray[level] = random.dice(1, 10)
        }

        hp = maxHp
    }

    override fun toString(): String {
        return "lvl: $level, hp: $hp/$maxHp, dmg: $damageDice, sight: $sightRange, str: $strength, dex: $dexterity," +
                " per: $perception, con: $constitution, int: $intellegence, wis: $wisdom, cha: $charisma," +
                " hpArray: ${hpArray.contentToString()}, ranged: $ranged, healDice: $healDice"
    }

    companion object {
        val mapper = mapperFor<Stats>()
    }
}