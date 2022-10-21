package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.MathUtils.clamp
import com.github.samelVhatargh.vapula.World
import com.github.samelVhatargh.vapula.components.FieldOfView
import com.github.samelVhatargh.vapula.components.Position
import com.github.samelVhatargh.vapula.components.SoundEffect
import com.github.samelVhatargh.vapula.components.SoundSet
import com.github.samelVhatargh.vapula.events.*
import com.github.samelVhatargh.vapula.notifier
import com.github.samelVhatargh.vapula.sounds.SoundEffectType
import com.github.samelVhatargh.vapula.sounds.queue.Queue
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

private const val AUDIBLE_DISTANCE = 5f
private const val VOLUME_OPTION = 1f

class Sound(
    assets: AssetManager,
    world: World
) : IteratingSystem(allOf(SoundEffect::class).get()), Observer {

    private val playerFov = world.player[FieldOfView.mapper]!!
    private val playerPosition = world.player[Position.mapper]!!
    private val queue = Queue(assets)

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        queue.play(VOLUME_OPTION)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val soundEffect = entity[SoundEffect.mapper]!!
        engine.removeEntity(entity)

        //You can only hear sounds if you see them
        if (!playerFov.isVisible(soundEffect.position)) {
            return
        }

        val distance = playerPosition.toVec2().dst(soundEffect.position.toVec2())
        val volume = clamp(1f - distance / AUDIBLE_DISTANCE, 0f, 1f)

        if (volume == 0f) {
            return
        }

        val pan = clamp((soundEffect.position.x - playerPosition.x) / AUDIBLE_DISTANCE, -1f, 1f)

        queue.addSound(soundEffect.type, volume, pan)
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
            is EntityAttacked -> {
                event.attacker[SoundSet.mapper]?.attack?.let {
                    addSound(it, event.attacker[Position.mapper])
                }
            }
            is EntityDamaged -> {
                event.attacker[SoundSet.mapper]?.hit?.let {
                    addSound(it, event.victim[Position.mapper])
                }
            }
            is EntityDied -> {
                event.victim[SoundSet.mapper]?.death?.let {
                    addSound(it, event.victim[Position.mapper])
                }
            }
            is EntityMoved -> {
                event.entity[SoundSet.mapper]?.move?.let {
                    addSound(it, event.newPosition)
                }
            }
        }
    }

    private fun addSound(soundEffectType: SoundEffectType, soundPosition: Position?) {
        if (soundPosition != null) {
            engine.entity {
                with<SoundEffect> {
                    type = soundEffectType
                    position = soundPosition
                }
            }
        }
    }

    override fun getSupportedTypes(): Array<EventType> =
        arrayOf(EventType.ENTITY_ATTACKED, EventType.ENTITY_DAMAGED, EventType.ENTITY_DIED, EventType.ENTITY_MOVED)
}