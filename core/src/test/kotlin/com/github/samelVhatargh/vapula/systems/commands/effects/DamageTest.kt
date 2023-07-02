package com.github.samelVhatargh.vapula.systems.commands.effects

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.github.samelVhatargh.vapula.game.statuses.DeadComponent
import com.github.samelVhatargh.vapula.graphics.GraphicsComponent
import com.github.samelVhatargh.vapula.game.statuses.InvulnerabilityComponent
import com.github.samelVhatargh.vapula.game.stats.StatsComponent
import com.github.samelVhatargh.vapula.game.commands.Damage
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
        val attacker = createEntity(startingHp)
        val entity = createEntity(startingHp)

        val damage = 5
        val damageCommand = Damage(TestNotifier(), attacker, entity, damage)

        damageCommand.execute()

        Assertions.assertEquals(startingHp - damage, entity[StatsComponent.mapper]!!.hp)
    }

    @Test
    fun `should not lower hp if entity is invulnerable`() {
        val startingHp = 100
        val attacker = createEntity(startingHp)
        val entity = createEntity(startingHp)
        entity.add(InvulnerabilityComponent())

        val damage = 50
        val damageCommand = Damage(TestNotifier(), attacker, entity, damage)

        damageCommand.execute()

        Assertions.assertEquals(startingHp, entity[StatsComponent.mapper]!!.hp)
    }

    @Test
    fun `should kill entity if hp reaches zero`() {
        val startingHp = 10
        val attacker = createEntity(startingHp)
        val entity = createEntity(startingHp)

        val notifier = TestNotifier()
        val damage = 50
        val damageCommand = Damage(notifier, attacker, entity, damage)

        damageCommand.execute()

        Assertions.assertTrue(entity.has(DeadComponent.mapper))
    }

    private fun createEntity(startingHp: Int): Entity {
        Gdx.app = GdxTestApplication()
        val stats = StatsComponent().apply {
            generateHp(startingHp)
        }

        val entity = Engine().entity().apply {
            add(stats)
            add(GraphicsComponent())
        }

        return entity
    }
}