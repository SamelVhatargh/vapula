package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Tile

data class Map(val tiles: Array<Array<Tile>>, val rooms: Collection<Room>, val tunnels: Collection<Tunnel>) {
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
}