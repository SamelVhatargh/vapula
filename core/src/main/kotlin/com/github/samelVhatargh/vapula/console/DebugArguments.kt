package com.github.samelVhatargh.vapula.console

import com.badlogic.gdx.Application

private data class StartupCommandDescription(val command: String, val argumentsCount: Int = 0)

class DebugArguments(private val args: Array<String>) {

    val logLevel: Int = if (args.contains("debug")) Application.LOG_DEBUG else Application.LOG_ERROR


    private val validStartupCommands = arrayOf(
        StartupCommandDescription("tyriok"),
        StartupCommandDescription("removeFog"),
        StartupCommandDescription("eye"),
        StartupCommandDescription("xy"),
        StartupCommandDescription("hemonugi")
    )


    fun getStartupCommands(): List<String> {
        val result = mutableListOf<String>()

        validStartupCommands.forEach {
            if (have(it.command)) {
                var command = it.command
                if (it.argumentsCount > 0) {
                    val commandIndex = args.indexOf(command)
                    for (i in 1..it.argumentsCount) {
                        command += " ${args[commandIndex + i]}"
                    }
                }
                result.add(command)
            }
        }

        return result
    }


    private fun have(argument: String): Boolean {
        return args.contains(argument)
    }
}