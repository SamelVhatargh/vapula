package com.github.samelVhatargh.vapula.map

import com.github.samelVhatargh.vapula.map.generators.Map
import com.github.samelVhatargh.vapula.tests.MapBaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class TilesTest : MapBaseTest() {

    companion object : MapBaseTest() {
        @JvmStatic
        fun emptyTilesDataProvider() = Stream.of(
            Arguments.of(
                3, 3, map(
                    "###",
                    "###",
                    "###"
                )
            ),
            Arguments.of(
                4, 4, map(
                    "####",
                    "####",
                    "####",
                    "####"
                )
            ),
            Arguments.of(
                3, 2, map(
                    "###",
                    "###"
                )
            )
        )
    }


    @ParameterizedTest(name = "empty map of {0}x{1} is {2}")
    @MethodSource("emptyTilesDataProvider")
    fun `createEmptyTiles() should create empty tiles with specified width and height`(
        width: Int,
        height: Int,
        expectedMap: Map
    ) {
        val emptyMap = Map(createEmptyTiles(width, height), listOf(), listOf())

        assertEquals(expectedMap, emptyMap)
    }
}
