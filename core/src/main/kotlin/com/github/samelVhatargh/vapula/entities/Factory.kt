package com.github.samelVhatargh.vapula.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.game.ai.AiComponent
import com.github.samelVhatargh.vapula.graphics.GraphicsComponent
import com.github.samelVhatargh.vapula.graphics.IdleAnimationComponent
import com.github.samelVhatargh.vapula.graphics.Layer
import com.github.samelVhatargh.vapula.map.*
import com.github.samelVhatargh.vapula.sounds.SoundSetComponent
import com.github.samelVhatargh.vapula.sounds.soundEffects.*
import com.github.samelVhatargh.vapula.utility.random
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger


enum class GoblinType(val role: String) {
    FIGHTER(""), ARCHER("Archer"), SHAMAN("Shaman")
}

class Factory(private val engine: Engine, var storey: Storey) {

    companion object {
        val log = logger<Factory>()
    }

    fun createPlayer(position: PositionComponent = getRandomEmptyPosition()): Entity {
        val player = engine.entity {
            with<GraphicsComponent> {
                spriteName = "character"
            }
            with<Player>()
            with<OccupySpace>()
            with<FieldOfViewComponent>()
            with<IdleAnimationComponent>()
            with<Name> {
                name = "player"
            }
            with<SoundSetComponent> {
                move = StepSound()
                attack = AttackSound(AttackType.MELEE)
                hit = HitSound(HitType.SLASH)
                death = DeathSound(Creature.PLAYER)
            }
        }

        position.z = storey.z
        player.add(position)

        val stats = Stats().apply {
            strength = random.dice("1d6 + 3")
            dexterity = random.dice("1d6 + 3")
            constitution = random.dice("1d6 + 3")
            perception = random.dice("1d6 + 3")
            intellegence = random.dice("1d6 + 3")
            wisdom = random.dice("1d6 + 3")
            charisma = random.dice("1d6 + 3")

            level = 1
            damageDice = 8
            generateHp(10, 10)
        }
        player.add(stats)
        log.debug { stats.toString() }

        return player
    }

    private var goblinCount = 0

    fun createGoblin(position: PositionComponent = getRandomEmptyPosition(), type: GoblinType = GoblinType.FIGHTER): Entity {
        goblinCount++
        val monster = engine.entity {
            with<GraphicsComponent> {
                spriteName = "goblin${type.role}"
            }
            with<OccupySpace>()
            with<Name> {
                name = "Goblin ${type.role} $goblinCount"
            }
            with<AiComponent>()
            with<IdleAnimationComponent>()
        }
        position.z = storey.z
        monster.add(position)

        val stats = Stats().apply {
            strength = random.dice("1d2")
            dexterity = random.dice("1d6 + 1")
            constitution = random.dice("1d4 - 1")
            perception = random.dice("1d4")
            intellegence = random.dice("1d2")
            wisdom = random.dice("1d2")
            charisma = random.dice("1d4")


            level = 1
            damageDice = 4
            healDice = if (type === GoblinType.SHAMAN) 4 else 0
            ranged = type !== GoblinType.FIGHTER
            projectileType = when (type) {
                GoblinType.ARCHER -> ProjectileType.ARROW
                GoblinType.SHAMAN -> ProjectileType.MAGIC
                else -> ProjectileType.NONE
            }

            generateHp(2)
        }
        monster.add(stats)

        val soundSet = SoundSetComponent().apply {
            move = StepSound()
            death = DeathSound(Creature.GOBLIN)
            when (stats.projectileType) {
                ProjectileType.ARROW -> {
                    attack = AttackSound(AttackType.RANGE)
                    hit = HitSound(HitType.PIERCE)
                }
                ProjectileType.MAGIC -> {
                    attack = AttackSound(AttackType.MAGIC)
                    hit = HitSound(HitType.MAGIC)
                }
                ProjectileType.NONE -> {
                    attack = AttackSound(AttackType.MELEE)
                    hit = HitSound(HitType.SLASH)
                }
            }
        }
        monster.add(soundSet)

        log.debug { "${monster[Name.mapper]!!.name} - $stats" }

        return monster
    }

    private fun getRandomEmptyPosition(): PositionComponent {
        val objects = engine.getEntitiesFor(OCCUPY_SPACE_FAMILY)

        var i = 1
        while (i < 1000) {
            val x = random.range(0 until storey.width)
            val y = random.range(0 until storey.height)

            if (storey.tiles[x][y].terrain == Terrain.FLOOR) {
                val position = PositionComponent(x, y, storey.z)

                if (objects.find {
                        it[PositionComponent.mapper]!! == position
                    } === null) return position
            }

            i++
        }

        return PositionComponent(5, 5)
    }

    fun createBarrel(position: PositionComponent = getRandomEmptyPosition()): Entity? {
        try {
            if (storey.tiles[position.x][position.y].terrain == Terrain.WALL) {
                return null
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return null
        }

        val barrel = engine.entity {
            with<GraphicsComponent> {
                spriteName = "barrel"
            }
            with<OccupySpace>()
        }
        position.z = storey.z
        barrel.add(position)

        return barrel
    }

    fun createTunnel(position: PositionComponent, sprite: String = "Railroad16") {
        val tunnel = engine.entity {
            with<GraphicsComponent> {
                spriteName = sprite
                layer = Layer.FLOOR
            }
            with<VisibleIfExploredAndOutOfFieldOfViewComponent>()
        }
        position.z = storey.z
        tunnel.add(position)
    }

    fun createStairs(up: Boolean, position: PositionComponent = getRandomEmptyPosition()) {
        val stairs = engine.entity {
            with<GraphicsComponent> {
                spriteName = if (up) "StairsUp" else "StairsDown"
                layer = Layer.FLOOR
            }
            with<VisibleIfExploredAndOutOfFieldOfViewComponent>()
        }
        position.z = storey.z
        stairs.add(position)
        stairs.add(if (up) GoUp() else GoDown())
    }

    /**
     * Creates [projectile][Entity] with specified [type] facing from [start] to [end]
     */
    fun createProjectile(start: PositionComponent, end: PositionComponent, type: ProjectileType): Entity {
        val arrow = engine.entity {
            with<GraphicsComponent> {
                spriteName = type.spriteName
                layer = Layer.FLOOR
                rotation = start.toVec2().sub(end.toVec2()).angleDeg() - 270f
            }
        }

        arrow.add(PositionComponent(start.x, start.y, start.z))
        return arrow
    }
}