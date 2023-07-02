package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Engine
import com.github.samelVhatargh.vapula.game.statuses.OccupySpaceComponent
import com.github.samelVhatargh.vapula.tests.DescribedMap
import com.github.samelVhatargh.vapula.tests.MapBaseTest
import ktx.ashley.entity
import ktx.ashley.with
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
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
                "To east with obstacle which blocks north path",
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
                "path to south-east with simple obstacle should bend around north wall",
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
                PositionComponent(0, 0),
                PositionComponent(100, 100)
            ),
            Arguments.of(
                "start is not present on map",
                PositionComponent(-100, -100),
                PositionComponent(0, 0)
            ),
            Arguments.of(
                "start and destination are not present on map",
                PositionComponent(-100, -100),
                PositionComponent(100, 100)
            ),
        )

        @JvmStatic
        fun pathFindingForMonstersDataProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "Straight line to east another monster in path",
                describedMap(
                    "......",
                    ".s.me.",
                    "......",
                ),
                path(
                    "...3..",
                    ".12.4.",
                    "......",
                ),
            ),
            Arguments.of(
                "Straight line to east with several monsters in path",
                describedMap(
                    "...m..",
                    ".s.me.",
                    "...m..",
                    "......",
                ),
                path(
                    "...m..",
                    ".1.m5.",
                    "..2m4.",
                    "...3..",
                ),
            ),
            Arguments.of(
                "Should go to last possible place if destination is surrounded",
                describedMap(
                    "............",
                    "............",
                    "......mmm...",
                    "......me....",
                    "...s..mmm...",
                    "............",
                    "............",
                ),
                path(
                    "............",
                    "............",
                    "......mmm...",
                    "......m98...",
                    "...123mmm7..",
                    "......456...",
                    "............",
                ),
            ),
            Arguments.of(
                "Should go around through second entrance if another monster blocking first entrance and new path is less then 11 steps",
                describedMap(
                    "##########..",
                    ".........#..",
                    "....####.#..",
                    "...sme...#..",
                    "....####.#..",
                    "#######.##..",
                ),
                path(
                    "##########..",
                    "....3456.#..",
                    "...2####7#..",
                    "...1m098.#..",
                    "....####.#..",
                    "#######.##..",
                ),
            ),
            Arguments.of(
                "Should not go around through second entrance if another monster blocking first entrance and second entrance path is to long",
                describedMap(
                    "#############..",
                    "............#..",
                    "....#######.#..",
                    "...sme......#..",
                    "....#######.#..",
                    "##########.##..",
                ),
                path(
                    "#############..",
                    "............#..",
                    "....#######.#..",
                    "...123......#..",
                    "....#######.#..",
                    "##########.##..",
                ),
            ),
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathFindingDataProvider")
    fun `path finding`(testDescription: String, describedMap: DescribedMap, expectedPath: Path) {
        val map = describedMap.map
        val storey = Storey(map)

        val pathFinder = PathFinder(storey, Engine())

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
    fun `should return empty path if`(testDescription: String, startPosition: PositionComponent, endPosition: PositionComponent) {
        val describedMap = describedMap(
            ".....",
            ".....",
        )

        val map = describedMap.map
        val storey = Storey(map)

        val pathFinder = PathFinder(storey, Engine())

        val path = pathFinder.findPath(startPosition, endPosition)

        assertTrue(path.isEmpty())
    }

    @Test
    fun `should return empty path if destination is wall`() {
        val describedMap = describedMap(
            "...##",
            "...##",
        )

        val map = describedMap.map
        val storey = Storey(map)

        val pathFinder = PathFinder(storey, Engine())
        val startPosition = PositionComponent(0, 0)
        val endPosition = PositionComponent(4, 1)

        val path = pathFinder.findPath(startPosition, endPosition)

        assertTrue(path.isEmpty())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathFindingForMonstersDataProvider")
    fun `path finding for monsters`(testDescription: String, describedMap: DescribedMap, expectedPath: Path) {
        val map = describedMap.map
        val storey = Storey(map)


        val startPosition = describedMap.getPosition('s')
        val endPosition = describedMap.getPosition('e')
        val monsters = describedMap.getPositions('m')

        val engine = Engine()
        monsters.forEach { position ->
            engine.entity {
                with<PositionComponent> {
                    x = position.x
                    y = position.y
                }
                with<OccupySpaceComponent>()
            }
        }


        val pathFinder = PathFinder(storey, engine)
        if (startPosition === null || endPosition === null) {
            fail<Assertions>("start or end positions cant be found")
            return
        }

        val path = pathFinder.findPath(startPosition, endPosition)

        assertEquals(expectedPath, path)
    }
}