package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Tile

data class Map(val tiles: Array<Array<Tile>>, val rooms: Collection<Room>, val tunnels: Collection<Tunnel>) {

    val width: Int
        get() = tiles.size

    val height: Int
        get() = tiles.first().size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Map

        if (!tiles.contentDeepEquals(other.tiles)) return false

        return true
    }

    override fun hashCode(): Int {
        return tiles.contentDeepHashCode()
    }

    override fun toString(): String {
        var result = ""

        for (x in 0 until width) {
            for (y in 0 until height) {
                result += tiles[x][y]
            }
            result += System.lineSeparator()
        }

        return System.lineSeparator() + result + System.lineSeparator()
    }
}