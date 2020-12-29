package com.github.samelVhatargh.vapula.components

import com.github.samelVhatargh.vapula.tests.MapBaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GameMapTest : MapBaseTest() {

    @Test
    fun `switchTile should change floor tile to wall tile`() {
        val map = map(
            "...",
            "..."
        )

        val gameMap = GameMap().apply {
            width = map.width
            height = map.height
            tiles = map.tiles
        }

        gameMap.switchTile(1, 1)

        Assertions.assertEquals(
            map(
                "...",
                ".#."
            ), map(gameMap)
        )
    }

    @Test
    fun `switchTile should change wall tile to floor tile`() {
        val map = map(
            ".#.",
            "..."
        )

        val gameMap = GameMap().apply {
            width = map.width
            height = map.height
            tiles = map.tiles
        }

        gameMap.switchTile(1, 0)

        Assertions.assertEquals(
            map(
                "...",
                "..."
            ), map(gameMap)
        )
    }
}