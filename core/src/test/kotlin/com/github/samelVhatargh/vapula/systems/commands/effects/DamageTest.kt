package com.github.samelVhatargh.vapula.systems.commands.effects

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.Invulnerability
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.systems.commands.Damage
import com.github.samelVhatargh.vapula.tests.GdxTestApplication
import com.github.samelVhatargh.vapula.tests.TestNotifier
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.has
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DamageTest {

    @Test
    fun `should lower hp for damage amount`() {
        val startingHp = 100
        val entity = createEntity(startingHp)

        val damage = 5
        val damageCommand = Damage(TestNotifier(), entity, damage)

        damageCommand.execute()

        Assertions.assertEquals(startingHp - damage, entity[Stats.mapper]!!.hp)
    }

    @Test
    fun `should not lower hp if entity is invulnerable`() {
        val startingHp = 100
        val entity = createEntity(startingHp)
        entity.add(Invulnerability())

        val damage = 50
        val damageCommand = Damage(TestNotifier(), entity, damage)

        damageCommand.execute()

        Assertions.assertEquals(startingHp, entity[Stats.mapper]!!.hp)
    }

    @Test
    fun `should kill entity if hp reaches zero`() {
        val startingHp = 10
        val entity = createEntity(startingHp)

        val notifier = TestNotifier()
        val damage = 50
        val damageCommand = Damage(notifier, entity, damage)

        damageCommand.execute()

        Assertions.assertTrue(entity.has(Dead.mapper))
    }

    private fun createEntity(startingHp: Int): Entity {
        Gdx.app = GdxTestApplication()
        val stats = Stats().apply {
            generateHp(startingHp)
        }

        val entity = Engine().entity().apply {
            add(stats)
            add(Graphics())
        }

        return entity
    }
}