package com.github.samelVhatargh.vapula.utility

import ktx.log.debug
import ktx.log.logger
import kotlin.random.Random

class SeededRandom(seed: Int = Random.Default.nextInt()) {

    companion object {
        val log = logger<SeededRandom>()
    }

    init {
        log.debug { "seed = $seed" }
    }

    private val generator = Random(seed)

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
}

val random = SeededRandom()