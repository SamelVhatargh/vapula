package com.github.samelVhatargh.vapula.events

import com.badlogic.ashley.core.Entity
import com.github.samelVhatargh.vapula.map.PositionComponent

interface Event {
    val type: EventType
}

enum class EventType {
    ENTITY_ATTACKED,
    ENTITY_DAMAGED,
    ENTITY_DIED,
    ENTITY_MOVED,
}

/**
 * Когда [одна сущность][attacker] атакует [другую][defender]
 */
data class EntityAttacked(val attacker: Entity, val defender: Entity, val miss: Boolean) : Event {
    override val type: EventType
        get() = EventType.ENTITY_ATTACKED
}

/**
 * When [entity][victim] receive [damage] from another [entity][attacker]
 */
data class EntityDamaged(val attacker: Entity, val victim: Entity, val damage: Int) : Event {
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

/**
 * When [entity] moves to from [old position][oldPosition] to [new position][newPosition]
 */
data class EntityMoved(val entity: Entity, val oldPosition: PositionComponent, val newPosition: PositionComponent) : Event {
    override val type: EventType
        get() = EventType.ENTITY_MOVED

}
