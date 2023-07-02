package com.github.samelVhatargh.vapula.map

import com.github.samelVhatargh.vapula.tests.MapBaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class StoreyTest : MapBaseTest() {

    @Test
    fun `switchTile should change floor tile to wall tile`() {
        val map = map(
            "...",
            "..."
        )

        val storey = Storey(map)

        storey.switchTile(1, 1)

        Assertions.assertEquals(
            map(
                "...",
                ".#."
            ), map(storey)
        )
    }

    @Test
    fun `switchTile should change wall tile to floor tile`() {
        val map = map(
            ".#.",
            "..."
        )

        val storey = Storey(map)

        storey.switchTile(1, 0)

        Assertions.assertEquals(
            map(
                "...",
                "..."
            ), map(storey)
        )
    }
}