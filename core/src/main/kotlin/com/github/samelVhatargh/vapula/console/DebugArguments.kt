package com.github.samelVhatargh.vapula.console

import com.badlogic.gdx.Application

class DebugArguments(private val args: Array<String>) {

    val logLevel: Int = if (args.contains("debug")) Application.LOG_DEBUG else Application.LOG_ERROR

    fun have(argument: String): Boolean {
        return args.contains(argument)
    }
}