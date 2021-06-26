package com.github.samelVhatargh.vapula.systems.commands

import com.github.samelVhatargh.vapula.systems.commands.effects.Effect

class DoNothing : Command {
    override fun execute(): Array<Effect> {
        return emptyArray()
    }
}