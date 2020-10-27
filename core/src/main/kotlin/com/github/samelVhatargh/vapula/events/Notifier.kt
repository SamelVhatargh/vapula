package com.github.samelVhatargh.vapula.events

import ktx.collections.*
import ktx.log.info
import ktx.log.logger

/**
 * Сообщает наблюдателям о наступлении определенных событий
 */
class Notifier {

    companion object {
        val log = logger<Notifier>()
    }

    private val observers = mutableMapOf<EventType, GdxArray<Observer>>()

    /**
     * Добавляет [наблюдателя][observer] на конкретный [тип события][eventType]
     */
    fun addObserver(eventType: EventType, observer: Observer) {
        var currentObservers = observers[eventType]
        if (currentObservers === null) {
            currentObservers = GdxArray()
            observers[eventType] = currentObservers
        }

        if (observer !in currentObservers) {
            currentObservers.add(observer)
        }
    }

    /**
     * Удаляет [наблюдателя][observer] из получателей [события][eventType]
     */
    fun removeObserver(eventType: EventType, observer: Observer) {
        val currentObservers = observers[eventType]
        if (currentObservers !== null && observer in currentObservers) {
            currentObservers.removeValue(observer, true)
        }
    }

    /**
     * Удаляет [наблюдателя][observer] из получателей всех событий
     */
    fun removeObserver(observer: Observer) {
        observers.values.forEach { it.removeValue(observer, true) }
    }

    /**
     * Уведомляет наблюдателей о наступлении [события][event]
     */
    fun notify(event: Event) {
        log.info { event.type.toString() }
        observers[event.type]?.forEach { it.onNotify(event) }
    }
}