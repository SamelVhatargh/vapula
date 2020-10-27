package com.github.samelVhatargh.vapula.events

interface Observer {
    fun onNotify(event: Event)

    fun getSupportedTypes(): Array<EventType>
}