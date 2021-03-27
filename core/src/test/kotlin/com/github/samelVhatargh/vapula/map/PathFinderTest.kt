package com.github.samelVhatargh.vapula.map

import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.tests.DescribedMap
import com.github.samelVhatargh.vapula.tests.MapBaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class PathFinderTest : MapBaseTest() {

    companion object : MapBaseTest() {
        @JvmStatic
        fun pathFindingDataProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "Straight line to east without obstacles",
                describedMap(
                    "......",
                    ".s..e.",
                    "......",
                ),
                path(
                    "......",
                    ".1234.",
                    "......",
                ),
            ),
            Arguments.of(
                "Straight line to west without obstacles",
                describedMap(
                    "......",
                    ".e..s.",
                    "......",
                ),
                path(
                    "......",
                    ".4321.",
                    "......",
                ),
            ),
            Arguments.of(
                "Straight line to south without obstacles",
                describedMap(
                    "...",
                    ".s.",
                    "...",
                    "...",
                    ".e.",
                    "...",
                ),
                path(
                    "...",
                    ".1.",
                    ".2.",
                    ".3.",
                    ".4.",
                    "...",
                ),
            ),
            Arguments.of(
                "Straight line to north without obstacles",
                describedMap(
                    "...",
                    ".e.",
                    "...",
                    "...",
                    ".s.",
                    "...",
                ),
                path(
                    "...",
                    ".4.",
                    ".3.",
                    ".2.",
                    ".1.",
                    "...",
                ),
            ),
            Arguments.of(
                "Diagonal line without obstacles",
                describedMap(
                    "s....",
                    ".....",
                    ".....",
                    "...e.",
                    ".....",
                ),
                path(
                    "1....",
                    ".2...",
                    "..3..",
                    "...4.",
                    ".....",
                ),
            ),
            Arguments.of(
                "Several steps to east and one step south without obstacles",
                describedMap(
                    "s....",
                    "....e",
                ),
                path(
                    "123..",
                    "...45",
                ),
            ),
            Arguments.of(
                "To east with simple obstacle",
                describedMap(
                    "...............",
                    "......#........",
                    "...s..#..e.....",
                    "......#........",
                    "...............",
                ),
                path(
                    "......4.........",
                    ".....3#5........",
                    "...12.#.67......",
                    "......#.........",
                    "................",
                ),
            ),
            Arguments.of(
                "To east with obstacle wich blocks norh path",
                describedMap(
                    "...............",
                    "...####........",
                    "...s..#..e.....",
                    "......#........",
                    "...............",
                ),
                path(
                    "................",
                    "...####.........",
                    "...12.#.67......",
                    ".....3#5........",
                    "......4.........",
                ),
            ),
            Arguments.of(
                "path to south-east with simple obstacle should bend arroun north wall",
                describedMap(
                    "...............",
                    "...s..#........",
                    "......#..e.....",
                    "......#........",
                    "...............",
                ),
                path(
                    "......4........",
                    "...123#5.......",
                    "......#.67.....",
                    "......#........",
                    "...............",
                ),
            ),
            Arguments.of(
                "no path",
                describedMap(
                    "...s..#........",
                    "......#..e.....",
                    "......#........",
                ),
                path(
                    "......#........",
                    "......#........",
                    "......#........",
                ),
            ),
        )

        @JvmStatic
        fun nullEdgesDataProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "destination is not present on map",
                Position(0, 0),
                Position(100, 100)
            ),
            Arguments.of(
                "start is not present on map",
                Position(-100, -100),
                Position(0, 0)
            ),
            Arguments.of(
                "start and destination are not present on map",
                Position(-100, -100),
                Position(100, 100)
            ),
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathFindingDataProvider")
    fun `path finding`(testDescription: String, describedMap: DescribedMap, expectedPath: List<Position>) {
        val map = describedMap.map
        val gameMap = GameMap().apply {
            width = map.width
            height = map.height
            tiles = map.tiles
        }

        val pathFinder = PathFinder(gameMap)

        val startPosition = describedMap.getPosition('s')
        val endPosition = describedMap.getPosition('e')

        if (startPosition === null || endPosition === null) {
            fail<Assertions>("start or end positions cant be found")
            return
        }

        val path = pathFinder.findPath(startPosition, endPosition)

        assertEquals(expectedPath, path)
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("nullEdgesDataProvider")
    fun `should return empty path if`(testDescription: String, startPosition: Position, endPosition: Position) {
        val describedMap = describedMap(
            ".....",
            ".....",
        )

        val map = describedMap.map
        val gameMap = GameMap().apply {
            width = map.width
            height = map.height
            tiles = map.tiles
        }

        val pathFinder = PathFinder(gameMap)

        val path = pathFinder.findPath(startPosition, endPosition)

        assertEquals(0, path.count())
    }

    @Test
    fun `should return empty path if destination is wall`() {
        val describedMap = describedMap(
            "...##",
            "...##",
        )

        val map = describedMap.map
        val gameMap = GameMap().apply {
            width = map.width
            height = map.height
            tiles = map.tiles
        }

        val pathFinder = PathFinder(gameMap)
        val startPosition = Position(0, 0)
        val endPosition = Position(4, 1)

        val path = pathFinder.findPath(startPosition, endPosition)

        assertEquals(0, path.count())
    }
}