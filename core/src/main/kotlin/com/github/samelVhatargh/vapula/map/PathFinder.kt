package com.github.samelVhatargh.vapula.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.entities.OCCUPY_SPACE_FAMILY
import com.github.samelVhatargh.vapula.getEntityAtPosition


private data class Node(val position: Position, val index: Int)

private class NodeConnection(val start: Node, val end: Node, val direction: Direction, val engine: Engine) :
    Connection<Node> {
    override fun getCost(): Float {
        val entity = engine.getEntityAtPosition(end.position, OCCUPY_SPACE_FAMILY)
        if (entity !== null) {
            return 10f
        }

        return when (direction) {
            Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH_EAST, Direction.SOUTH_WEST -> 1.414f
            else -> 1f
        }
    }

    override fun getFromNode(): Node = start
    override fun getToNode(): Node = end
}

private class PositionHeuristic : Heuristic<Node> {
    override fun estimate(node: Node, endNode: Node): Float {
        return Vector2.dst(
            node.position.x.toFloat(),
            node.position.y.toFloat(),
            endNode.position.x.toFloat(),
            endNode.position.y.toFloat()
        )
    }
}

private class StoreyGraph(val storey: Storey, val engine: Engine) : IndexedGraph<Node> {

    val nodes = mutableMapOf<Int, Node>()
    val indexes = mutableMapOf<Position, Int>()

    init {
        var i = 0
        storey.tiles.flatten().forEach { tile ->
            val position = tile.position
            position.z = storey.z
            val node = Node(position, i)
            nodes[i] = node
            indexes[position] = i
            i++
        }
    }

    override fun getConnections(start: Node): Array<Connection<Node>> {
        val connections = Array<Connection<Node>>()
        val startNode = nodes[start.index]
        if (startNode === null) {
            return connections
        }
        Direction.values().filter { it != Direction.NONE }.forEach { direction ->
            val neighbor = storey.getNeighbor(startNode.position, direction)
            if (neighbor.terrain === Terrain.FLOOR) {
                val end = nodes[indexes[neighbor.position]]
                if (end !== null) {
                    connections.add(NodeConnection(start, end, direction, engine))
                }
            }
        }

        return connections
    }

    override fun getNodeCount(): Int = nodes.count()
    override fun getIndex(node: Node): Int = node.index
}


class PathFinder(val storey: Storey, engine: Engine) {

    private val graph = StoreyGraph(storey, engine)

    private val apiPathFinder = IndexedAStarPathFinder(graph)

    fun findPath(start: Position, end: Position): Path {
        val path = DefaultGraphPath<Node>()
        val startNode = graph.nodes[graph.indexes[start]]
        val endNode = graph.nodes[graph.indexes[end]]

        if (endNode === null || startNode === null) {
            return Path()
        }

        apiPathFinder.searchNodePath(startNode, endNode, PositionHeuristic(), path)

        return Path(path.nodes.map { node -> node.position })
    }
}

class Path(private val positions: List<Position> = listOf()) {

    fun isEmpty(): Boolean = positions.isEmpty()

    fun getNextPosition(currentPosition: Position): Position {
        var i = 0
        positions.forEach {
            if (it == currentPosition) {
                if (i + 1 == positions.count()) {
                    return positions[i]
                }

                return positions[i + 1]
            }
            i++
        }

        return positions[0]
    }

    fun getLastPosition(): Position {
        return positions.last()
    }

    override fun hashCode(): Int {
        return positions.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Path

        if (positions != other.positions) return false

        return true
    }
}