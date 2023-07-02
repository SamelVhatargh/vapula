package com.github.samelVhatargh.vapula.map.generators

import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.utility.random
import kotlin.math.min

interface MapObject {
    fun getRandomPosition(): PositionComponent
    fun getCenter(): PositionComponent
}

class Room(val position: PositionComponent, val width: Int, val height: Int) : MapObject {
    override fun getRandomPosition(): PositionComponent {
        return PositionComponent(
            position.x + random.range(1 until width),
            position.y + random.range(1 until height)
        )
    }

    override fun getCenter(): PositionComponent {
        return PositionComponent(
            position.x + width / 2,
            position.y + height / 2
        )
    }

    fun getRandomCorner(): PositionComponent {
        val x = if (random.range(0..1) == 0) position.x else position.x + width - 1
        val y = if (random.range(0..1) == 0) position.y else position.y + height - 1

        return PositionComponent(x, y)
    }

    override fun toString(): String {
        return "p = (${position.x};${position.x}); w = $width; h = $height"
    }
}

/**
 * Создает комнату внутри указанных координат. Комната не может занимать граничные координаты
 */
fun createRoom(x1: Int, y1: Int, x2: Int, y2: Int, minSize: Int, maxSize: Int): Room {
    val regionWidth = x2 - x1 - 1
    val regionHeight = y2 - y1 - 1

    val maxRoomWidth = min(regionWidth, maxSize)
    val maxRoomHeight = min(regionHeight, maxSize)
    if (maxRoomHeight < minSize || maxRoomWidth < minSize) {
        throw Exception("Слишком мало место чтобы создать комнату ($x1, $y1; $x2, $y2)")
    }

    val width = random.range(minSize..maxRoomWidth)
    val height = random.range(minSize..maxRoomHeight)

    var x = x1 + 1
    var y = y1 + 1

    val maxX = regionWidth - width
    val maxY = regionHeight - height

    if (maxX > 0) {
        x += random.range(0..maxX)
    }
    if (maxY > 0) {
        y += random.range(0..maxY)
    }

    return Room(PositionComponent(x, y), width, height)
}

class Tunnel(private val start: PositionComponent, private val end: PositionComponent, val tiles: Collection<PositionComponent>) : MapObject {
    override fun getRandomPosition(): PositionComponent {
        return random.collection(tiles)
    }

    override fun getCenter(): PositionComponent {
        return PositionComponent(
            start.x + (end.x - start.x) / 2,
            start.y + (end.y - start.y) / 2
        )
    }

    override fun toString(): String {
        return tiles.toString()
    }
}