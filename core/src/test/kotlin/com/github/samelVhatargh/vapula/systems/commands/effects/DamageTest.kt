package com.github.samelVhatargh.vapula.systems.commands.effects

import com.badlogic.ashley.core.Engine
import com.github.samelVhatargh.vapula.components.Invulnerability
import com.github.samelVhatargh.vapula.components.Stats
import com.github.samelVhatargh.vapula.tests.TestNotifier
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DamageTest {

    @Test
    fun `should lower hp for damage amount`() {
        val startingHp = 100
        val entity = Engine().entity {
            with<Stats> {
                hp = startingHp
            }
        }

        val damage = 5
        val damageCommand = Damage(TestNotifier(), entity, damage)

        damageCommand.apply()

        Assertions.assertEquals(startingHp - damage, entity[Stats.mapper]!!.hp)
    }

    @Test
    fun `should not lower hp if entity is invulnerable`() {
        val startingHp = 100
        val entity = Engine().entity {
            with<Stats> {
                hp = startingHp
            }
            with<Invulnerability>()
        }

        val damage = 50
        val damageCommand = Damage(TestNotifier(), entity, damage)

        damageCommand.apply()

        Assertions.assertEquals(startingHp, entity[Stats.mapper]!!.hp)
    }

    @Test
    fun `should return kill effect if hp reaches zero`() {
        val startingHp = 10
        val entity = Engine().entity {
            with<Stats> {
                hp = startingHp
            }
        }

        val notifier = TestNotifier()
        val damage = 50
        val damageCommand = Damage(notifier, entity, damage)

        val effects = damageCommand.apply()

        Assertions.assertTrue(effects[0] is Kill)
    }
}