package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.Tile
import com.github.samelVhatargh.vapula.map.createEmptyTiles
import com.github.samelVhatargh.vapula.utility.random


class BSPDungeon(
    private val minRoomSize: Int = 4,
    private val maxRoomSize: Int = 7,
    private val splitRatio: Float = 0.15f
) : MapGenerator {

    private lateinit var digger: Digger

    override fun getTiles(width: Int, height: Int): Array<Array<Tile>> {
        val tiles = createEmptyTiles(width, height)
        digger = Digger(tiles)

        val leaf = Leaf(0, 0, width - 1, height - 1)
        splitLeaf(leaf)
        connectLeaves(tiles, leaf)

        return tiles
    }

    private fun splitLeaf(leaf: Leaf) {
        leaf.split(minRoomSize, splitRatio)
        if (leaf.hasChildren()) {
            splitLeaf(leaf.firstLeaf)
            splitLeaf(leaf.secondLeaf)
        }
    }

    private fun connectLeaves(tiles: Array<Array<Tile>>, parentLeaf: Leaf) {
        if (!parentLeaf.hasChildren()) {
            return
        }

        val firstLeaf = parentLeaf.firstLeaf
        val secondLeaf = parentLeaf.secondLeaf

        connectLeaves(tiles, firstLeaf)
        connectLeaves(tiles, secondLeaf)

        if (!firstLeaf.containRoom()) {
            val room = createRoom(firstLeaf.x1, firstLeaf.y1, firstLeaf.x2, firstLeaf.y2, minRoomSize, maxRoomSize)
            firstLeaf.room = room
            digger.dig(room)
        }

        if (!secondLeaf.containRoom()) {
            val room =
                createRoom(secondLeaf.x1, secondLeaf.y1, secondLeaf.x2, secondLeaf.y2, minRoomSize, maxRoomSize)
            secondLeaf.room = room
            digger.dig(room)
        }

        if (firstLeaf.room !== null && secondLeaf.room !== null) {
            parentLeaf.tunnel = digger.dig(firstLeaf.room!!, secondLeaf.room!!)
        } else {
            val objects = parentLeaf.getClosestMapObjectsFromSeparateLeaves()
            parentLeaf.tunnel = digger.dig(objects.first(), objects.last())
        }
    }
}

private class Leaf(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {

    var leaves: Array<Leaf>? = null

    val firstLeaf: Leaf
        get() = leaves!![0]
    val secondLeaf: Leaf
        get() = leaves!![1]

    val width = x2 - x1
    val height = y2 - y1

    var room: Room? = null
    var tunnel: Tunnel? = null

    fun split(minRoomSize: Int, splitRatio: Float) {
        if (random.range(1..2) == 1) {
            splitVertically(minRoomSize, splitRatio)
        } else {
            splitHorizontally(minRoomSize, splitRatio)
        }
    }

    private fun splitHorizontally(minRoomSize: Int, splitRatio: Float, tryToSplitAnotherWay: Boolean = true) {
        val center = height / 2
        val splitPosition = y1 + (center - height * random.range(-splitRatio..splitRatio)).toInt()

        if (splitPosition - y1 < (minRoomSize + 2) || y2 - splitPosition < (minRoomSize + 2)) {
            if (tryToSplitAnotherWay) {
                splitVertically(minRoomSize, splitRatio, false)
            }

            return
        }

        val firstLeaf = Leaf(x1, y1, x2, splitPosition)
        val secondLeaf = Leaf(x1, splitPosition + 1, x2, y2)

        leaves = arrayOf(firstLeaf, secondLeaf)
    }

    private fun splitVertically(minRoomSize: Int, splitRatio: Float, tryToSplitAnotherWay: Boolean = true) {
        val center = width / 2
        val splitPosition = x1 + (center - width * random.range(-splitRatio..splitRatio)).toInt()

        if (splitPosition - x1 < (minRoomSize + 2) || x2 - splitPosition < (minRoomSize + 2)) {
            if (tryToSplitAnotherWay) {
                splitHorizontally(minRoomSize, splitRatio, false)
            }

            return
        }

        val firstLeaf = Leaf(x1, y1, splitPosition, y2)
        val secondLeaf = Leaf(splitPosition + 1, y1, x2, y2)

        leaves = arrayOf(firstLeaf, secondLeaf)
    }

    fun hasChildren(): Boolean = leaves !== null

    /**
     * Содержит ли ветка или под-ветки комнату
     */
    fun containRoom(): Boolean {
        if (room !== null) {
            return true
        }

        if (hasChildren()) {
            return firstLeaf.containRoom() || secondLeaf.containRoom()
        }

        return false
    }

    /**
     * Возвращает все комнаты или тоннели, который находятся в текущей ветке
     */
    private fun getAllMapObjects(): Collection<MapObject> {
        val objects = mutableListOf<MapObject>()

        if (room != null) {
            objects.add(room!!)
        }

        if (tunnel != null) {
            objects.add(tunnel!!)
        }

        if (hasChildren()) {
            objects.addAll(firstLeaf.getAllMapObjects())
            objects.addAll(secondLeaf.getAllMapObjects())
        }

        return objects
    }

    /**
     * Возвращает ближайшие друг к другу комнаты или тоннели из разных веток текущей ветки
     */
    fun getClosestMapObjectsFromSeparateLeaves(): Collection<MapObject> {
        val firstLeafObjects = firstLeaf.getAllMapObjects()
        val secondLeafObjects = secondLeaf.getAllMapObjects()

        var minDistance = 100000000f
        var collection = listOf<MapObject>()

        for (first in firstLeafObjects) {
            for (second in secondLeafObjects) {
                val distance = first.getCenter().distanceTo(second.getCenter())
                if (distance < minDistance) {
                    minDistance = distance
                    collection = listOf(first, second)
                }
            }
        }

        return collection
    }
}