package com.github.samelVhatargh.vapula.map

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.Heuristic
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.github.samelVhatargh.vapula.components.GameMap
import com.github.samelVhatargh.vapula.components.Position


private data class Node(val position: Position, val index: Int)

private class NodeConnection(val start: Node, val end: Node, val direction: Direction) : Connection<Node> {
    override fun getCost(): Float = when (direction) {
        Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH_EAST, Direction.SOUTH_WEST -> 1.414f
        else -> 1f
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

private class GameMapGraph(val gameMap: GameMap) : IndexedGraph<Node> {

    val nodes = mutableMapOf<Int, Node>()
    val indexes = mutableMapOf<Position, Int>()

    init {
        var i = 0
        gameMap.tiles.flatten().forEach { tile ->
            val position = tile.position
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
            val neighbor = gameMap.getNeighbor(startNode.position, direction)
            if (neighbor.terrain === Terrain.FLOOR) {
                val end = nodes[indexes[neighbor.position]]
                if (end !== null) {
                    connections.add(NodeConnection(start, end, direction))
                }
            }
        }

        return connections
    }

    override fun getNodeCount(): Int = nodes.count()
    override fun getIndex(node: Node): Int = node.index
}


class PathFinder(val gameMap: GameMap) {

    private val graph = GameMapGraph(gameMap)

    private val apiPathFinder = IndexedAStarPathFinder(graph)

    fun findPath(start: Position, end: Position): List<Position> {
        val path = DefaultGraphPath<Node>()
        val startNode = graph.nodes[graph.indexes[start]]
        val endNode = graph.nodes[graph.indexes[end]]

        if (endNode === null || startNode === null) {
            return listOf()
        }

        apiPathFinder.searchNodePath(startNode, endNode, PositionHeuristic(), path)

        return path.nodes.map { node -> node.position }
    }
}