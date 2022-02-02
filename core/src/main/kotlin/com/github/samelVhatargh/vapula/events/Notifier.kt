package com.github.samelVhatargh.vapula.events

import ktx.collections.GdxArray
import ktx.collections.contains
import ktx.log.info
import ktx.log.logger
import kotlin.collections.set

interface NotifierInterface {
    /**
     * Уведомляет наблюдателей о наступлении [события][event]
     */
    fun notify(event: Event)
}

/**
 * Сообщает наблюдателям о наступлении определенных событий
 */
class Notifier : NotifierInterface {

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
     * Добавляет [наблюдателя][observer] на все события которые он поддерживает
     */
    fun addObserver(observer: Observer) {
        observer.getSupportedTypes().forEach {
            addObserver(it, observer)
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
    override fun notify(event: Event) {
        observers[event.type]?.forEach { it.onNotify(event) }
    }
}