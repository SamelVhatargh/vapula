package com.github.samelVhatargh.vapula.graphics

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils.floor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.map.PositionComponent
import ktx.ashley.mapperFor
import ktx.math.vec2

data class Transition(val point: Vector2, var progress: Float = 0f)

abstract class AnimationDescription {
    abstract val start: Vector2
    protected abstract val transitions: Array<Transition>
    abstract val speed: Float
    open val interpolation: Interpolation = Interpolation.linear
    val transitionProgressFactor: Int
        get() = transitions.size
    open val moveCamera = false
    open val destroyEntityOnComplete = false

    fun getCurrentTransition(progress: Float): Transition? {
        if (transitions.isEmpty()) {
            return null
        }

        val chunkSize = 1f / transitions.size
        val currentChunk = floor(progress / chunkSize)

        if (currentChunk < 0 || currentChunk >= transitions.size) {
            return null
        }

        return transitions[currentChunk]
    }
}

class NoAnimation : AnimationDescription() {
    override val start = vec2(0f, 0f)
    override val transitions = emptyArray<Transition>()
    override val speed = 0f
}

open class WalkAnimation(start: PositionComponent, end: PositionComponent) : AnimationDescription() {
    override val start = start.toVec2()
    override val transitions = arrayOf(Transition(end.toVec2()))
    override val moveCamera = true
    override val speed = .15f
    override val interpolation: Interpolation = Interpolation.sine
}

class ProjectileAnimation(start: PositionComponent, end: PositionComponent) : WalkAnimation(start, end) {
    override val speed = .2f
    override val moveCamera = false
    override val destroyEntityOnComplete = true
}

class AttackAnimation(attacker: PositionComponent, target: PositionComponent) : AnimationDescription() {
    override val start = attacker.toVec2()
    override val transitions by lazy {
        val a = attacker.toVec2()
        val b = target.toVec2()
        val attackPoint = a.add(b.sub(a).limit(.3f))
        arrayOf(
            Transition(attackPoint),
            Transition(attacker.toVec2())
        )
    }
    override val speed = .1f
}

class DamageAnimation(position: PositionComponent) : AnimationDescription() {
    override val start = position.toVec2()
    override val transitions = arrayOf(
        Transition(vec2(position.x + .1f, position.y.toFloat())),
        Transition(vec2(position.x - .15f, position.y.toFloat())),
        Transition(position.toVec2()),
    )
    override val speed = .1f
}

class AnimationComponent(var description: AnimationDescription) : Component, Pool.Poolable {

    var startVector = description.start
    var animatedVector = startVector
    var progress = 0f
    val transition: Transition?
        get() = description.getCurrentTransition(progress)

    override fun reset() {
        description = NoAnimation()
        startVector = vec2(0f, 0f)
        animatedVector = startVector
        progress = 0f
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}