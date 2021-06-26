package com.github.samelVhatargh.vapula.systems.commands

import com.github.samelVhatargh.vapula.systems.commands.effects.Effect

/**
 * An action that can be performed by an [entity][com.badlogic.ashley.core.Entity]
 */
interface Command {

    /**
     * Return array of [effects][Effect] which are caused by execution of this command
     */
    fun execute(): Array<Effect>
}