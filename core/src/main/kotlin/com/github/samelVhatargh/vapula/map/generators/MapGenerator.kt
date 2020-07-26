package com.github.samelVhatargh.vapula.map.generators

interface MapGenerator {
    fun generate(width: Int, height: Int): Map
}