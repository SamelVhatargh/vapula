package com.github.samelVhatargh.vapula.events

interface Observer {
    fun onNotify(event: Event)
}