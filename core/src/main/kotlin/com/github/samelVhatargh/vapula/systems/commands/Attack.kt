package com.github.samelVhatargh.vapula.systems.commands

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.entities.Factory
import com.github.samelVhatargh.vapula.events.EntityAttacked
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.sounds.HitSound
import com.github.samelVhatargh.vapula.sounds.HitType
import com.github.samelVhatargh.vapula.sounds.MeleeAttackSound
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

class Attack(
    private val engine: Engine,
    private val attacker: Entity,
    private val defender: Entity,
    private val entityFactory: Factory
) : Command {

    override fun execute() {
        val attackerStats = attacker[Stats.mapper]!!
        val defenderStats = defender[Stats.mapper]!!
        val attackerPosition = attacker[Position.mapper]!!
        val defenderPosition = defender[Position.mapper]!!

        var hitChance = 65
        hitChance += (attackerStats.dexterity + (attackerStats.perception / 2)) * 5
        hitChance -= (defenderStats.perception + (defenderStats.dexterity / 2)) * 5

        val hit = random.chance(hitChance)
        attacker.add(Animation(AttackAnimation(attackerPosition, defenderPosition)))

        if (attackerStats.ranged) {
            var targetPosition = Position(defenderPosition.x, defenderPosition.y, defenderPosition.z)
            if (!hit) {
                targetPosition += Direction.values().random()
            }
            val arrow = entityFactory.createProjectile(attackerPosition, targetPosition, attackerStats.projectileType)
            arrow.add(Animation(ProjectileAnimation(attackerPosition, targetPosition)))
        } else {
            engine.entity {
                with<SoundEffect> {
                    type = MeleeAttackSound()
                    position = attackerPosition
                }
            }
        }

        if (!hit) {
            engine.notifier.notify(EntityAttacked(attacker, defender, true))
            return
        }

        var hitType = HitType.SLASH
        if (attackerStats.ranged) {
            hitType = HitType.PIERCE
        }
        if (attackerStats.projectileType === ProjectileType.MAGIC) {
            hitType = HitType.MAGIC
        }

        engine.entity {
            with<SoundEffect> {
                type = HitSound(hitType)
                position = defenderPosition
            }
        }

        val damage = (1..attackerStats.damageDice).random() + (attackerStats.strength / 2)
        engine.notifier.notify(EntityAttacked(attacker, defender, false))

        Damage(engine.notifier, defender, damage).execute()
    }
}