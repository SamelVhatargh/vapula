package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Tile

interface MapGenerator {
    fun getTiles(width: Int, height: Int): Array<Array<Tile>>
}