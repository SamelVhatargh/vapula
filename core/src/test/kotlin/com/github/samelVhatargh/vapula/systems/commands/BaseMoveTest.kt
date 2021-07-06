package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.OccupySpace
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.Storey
import com.github.samelVhatargh.vapula.tests.DescribedMap
import com.github.samelVhatargh.vapula.tests.MapBaseTest
import ktx.ashley.entity
import ktx.ashley.get
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class TestBaseMove(
    private val engine: Engine,
    private val storey: Storey,
    val entity: Entity,
    private val newPosition: Position
) : BaseMove() {
    override fun execute() {
        return changePosition(engine, storey, entity, newPosition)
    }
}

internal class BaseMoveTest : MapBaseTest() {

    private val engine = Engine()

    @Test
    fun `should move if there is no wall or obstacles`() {
        val describedMap = describedMap(
            "#####",
            "#...#",
            "#.e.#",
            "#...#",
            "#####",
        )
        val move = getMoveToEastCommand(describedMap)

        move.execute()
        Assertions.assertEquals(describedMap.getPosition('e')!! + Direction.EAST, move.entity[Position.mapper]!!)
    }

    @Test
    fun `should not move if there is wall`() {
        val describedMap = describedMap(
            "#####",
            "#...#",
            "#..e#",
            "#...#",
            "#####",
        )
        val move = getMoveToEastCommand(describedMap)

        Assertions.assertEquals(describedMap.getPosition('e')!!, move.entity[Position.mapper]!!)
    }

    @Test
    fun `should not move if there is an obstacle`() {
        val describedMap = describedMap(
            "#####",
            "#...#",
            "#.eo#",
            "#...#",
            "#####",
        )
        engine.entity().apply {
            add(describedMap.getPosition('o'))
            add(OccupySpace())
        }
        val move = getMoveToEastCommand(describedMap)

        Assertions.assertEquals(describedMap.getPosition('e')!!, move.entity[Position.mapper]!!)
    }

    private fun getMoveToEastCommand(describedMap: DescribedMap): TestBaseMove {
        val storey = Storey(describedMap.map)
        val entityPosition = describedMap.getPosition('e')!!
        val entity = engine.entity().apply {
            add(entityPosition)
        }
        val newPosition = entityPosition + Direction.EAST

        return TestBaseMove(engine, storey, entity, newPosition)
    }
}