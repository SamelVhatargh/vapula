package com.github.samelVhatargh.vapula.utility

import ktx.log.debug
import ktx.log.logger
import kotlin.math.max
import kotlin.random.Random

class SeededRandom(seed: Int = Random.Default.nextInt()) {

    companion object {
        val log = logger<SeededRandom>()
    }

    init {
        log.debug { "seed = $seed" }
    }

    private var generator = Random(seed)

    fun setSeed(seed: Int) {
        generator = Random(seed)
        log.debug { "new seed = $seed" }
    }

    fun <T> collection(collection: Collection<T>): T {
        if (collection.size == 1) {
            return collection.first()
        }

        val index = generator.nextInt(0, collection.size)

        return collection.elementAt(index)
    }

    fun range(range: IntRange): Int {
        if (range.first == range.last) {
            return range.first
        }

        return generator.nextInt(range.first, range.last + range.step)
    }

    fun range(range: ClosedRange<Float>): Float {
        return generator.nextFloat() * (range.endInclusive - range.start) + range.start
    }

    fun dice(number: Int, dice: Int, modifier: Int = 0): Int {
        var result = modifier
        for (throwNumber in 1..number) {
            result += range(1..dice)
        }

        return max(result, 1)
    }

    fun dice(value: String): Int {
        val values = value.replace(" ", "").split("d", "+", "-")
        if (values.count() < 2) {
            return 1
        }

        val number = values[0].toInt()
        val dice = values[1].toInt()
        var modifier = 0
        if (values.count() > 2) {
            modifier = values[2].toInt()
        }

        if (value.indexOf("-") != -1) {
            modifier = 0 - modifier
        }

        return dice(number, dice, modifier)
    }
}

val random = SeededRandom()