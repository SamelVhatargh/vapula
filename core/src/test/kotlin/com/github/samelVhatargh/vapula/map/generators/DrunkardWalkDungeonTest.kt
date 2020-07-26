package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Terrain
import com.github.samelVhatargh.vapula.map.Tile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DrunkardWalkDungeonTest {

    @Test
    fun `3x3 map should have floor tile in center`() {
        val generator = DrunkardWalkDungeon(1f)
        val map = generator.generate(3, 3)

        assertEquals(
            listOf(
                "###",
                "#.#",
                "###"
            ).toString(),
            toString(map.tiles)
        )
    }

    private fun toString(tiles: Array<Array<Tile>>): String {
        val stringTiles = mutableListOf<String>()
        for (x in tiles.indices) {
            var row = ""
            for (y in tiles[x].indices) {
                row += if (tiles[x][y].terrain == Terrain.WALL) "#" else "."
            }
            stringTiles.add(row)
        }

        return stringTiles.toString()
    }
}