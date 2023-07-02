package com.github.samelVhatargh.vapula.graphics

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.*
import com.github.samelVhatargh.vapula.entities.ANIMATION_FAMILY
import com.github.samelVhatargh.vapula.events.*
import com.github.samelVhatargh.vapula.map.Direction
import com.github.samelVhatargh.vapula.map.FieldOfViewComponent
import com.github.samelVhatargh.vapula.map.PositionComponent
import com.github.samelVhatargh.vapula.notifier
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.plusAssign
import ktx.ashley.remove
import ktx.math.vec2

/**
 * This system updates entity [GraphicsComponent.position] according to its current [AnimationComponent]
 */
class AnimationSystem(private val world: World) : IteratingSystem(ANIMATION_FAMILY), Observer {

    private val player = world.player

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[PositionComponent.mapper]!!
        val graphics = entity[GraphicsComponent.mapper]!!
        if (position.z != world.storey.z) {
            removeAnimation(entity)
            return
        }

        val fov = player[FieldOfViewComponent.mapper]!!
        if (!fov.isVisible(position)) {
            removeAnimation(entity)
            return
        }

        graphics.position = animate(entity, deltaTime)
    }

    private fun animate(entity: Entity, deltaTime: Float): Vector2? {
        val animation = entity[AnimationComponent.mapper] ?: return null

        val speed = animation.description.speed

        val transition = animation.transition
        if (transition === null) {
            removeAnimation(entity)
            return null
        }

        animation.animatedVector = vec2(animation.startVector.x, animation.startVector.y)
        animation.animatedVector.interpolate(transition.point, transition.progress, animation.description.interpolation)

        transition.progress += (deltaTime / speed) * animation.description.transitionProgressFactor
        animation.progress += (deltaTime / speed)

        if (animation.progress >= 1f) {
            removeAnimation(entity)
            return null
        }

        return animation.animatedVector
    }

    private fun removeAnimation(entity: Entity) {
        val animation = entity.remove<AnimationComponent>()
        entity[GraphicsComponent.mapper]!!.position = null
        if (animation is AnimationComponent && animation.description.destroyEntityOnComplete) {
            engine.removeEntity(entity)
        }
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.notifier.addObserver(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.notifier.removeObserver(this)
    }

    override fun onNotify(event: Event) {
        when (event) {
            is EntityMoved -> {
                addAnimation(event.entity, WalkAnimation(event.oldPosition, event.newPosition))
            }
            is EntityDamaged -> {
                if (event.damage > 0 && !event.victim.has(AnimationComponent.mapper) && event.victim.has(PositionComponent.mapper)) {
                    addAnimation(event.victim, DamageAnimation(event.victim[PositionComponent.mapper]!!))
                }
            }
            is EntityAttacked -> {
                val attacker = event.attacker
                val attackerPosition = attacker[PositionComponent.mapper]
                val defenderPosition = event.defender[PositionComponent.mapper]
                val attackerStats = attacker[Stats.mapper]

                if (attackerPosition != null && defenderPosition != null && attackerStats != null) {
                    addAnimation(attacker, AttackAnimation(attackerPosition, defenderPosition))

                    if (attackerStats.ranged) {
                        var targetPosition = PositionComponent(defenderPosition.x, defenderPosition.y, defenderPosition.z)
                        if (event.miss) {
                            targetPosition += Direction.values().random()
                        }
                        val arrow = world.entityFactory.createProjectile(
                            attackerPosition, targetPosition, attackerStats.projectileType
                        )
                        addAnimation(arrow, ProjectileAnimation(attackerPosition, targetPosition))
                    }
                }
            }
        }
    }

    override fun getSupportedTypes(): Array<EventType> =
        arrayOf(EventType.ENTITY_DAMAGED, EventType.ENTITY_ATTACKED, EventType.ENTITY_MOVED)

    private fun addAnimation(entity: Entity, animationDescription: AnimationDescription) {
            entity += AnimationComponent(animationDescription)
    }
}