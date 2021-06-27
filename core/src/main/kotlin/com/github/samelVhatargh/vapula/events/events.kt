package com.github.samelVhatargh.vapula.events

import com.badlogic.ashley.core.Entity

interface Event {
    val type: EventType
}

enum class EventType {
    ENTITY_ATTACKED,
    ENTITY_DAMAGED,
    ENTITY_DIED
}

/**
 * Когда [одна сущность][attacker] атакует [другую][defender]
 */
data class EntityAttacked(val attacker: Entity, val defender: Entity, val miss: Boolean) : Event {
    override val type: EventType
        get() = EventType.ENTITY_ATTACKED
}

/**
 * Когда [сущность][victim] получает [урон][damage]
 */
data class EntityDamaged(val victim: Entity, val damage: Int) : Event {
    override val type: EventType
        get() = EventType.ENTITY_DAMAGED
}

/**
 * When [entity][healer] heals another [entity][target] for [ammount][hp]
 */
data class EntityHealed(val healer: Entity, val target: Entity, val hp: Int) : Event {
    override val type: EventType
        get() = EventType.ENTITY_DAMAGED
}

/**
 * Когда [сущность][victim] умирает
 */
data class EntityDied(val victim: Entity) : Event {
    override val type: EventType
        get() = EventType.ENTITY_DIED
}
