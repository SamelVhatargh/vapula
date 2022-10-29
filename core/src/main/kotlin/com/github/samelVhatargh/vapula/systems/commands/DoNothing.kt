package com.github.samelVhatargh.vapula.systems.commands

class DoNothing : Command {
    override fun execute(): Boolean {
        return false
    }
}