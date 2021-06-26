package com.github.samelVhatargh.vapula.systems.commands.effects

/**
 * [Command][com.github.samelVhatargh.vapula.systems.commands.Command] execution result
 */
interface Effect {

    /**
     * Applies effect and returns additional effects if needed
     */
    fun apply() : Array<Effect>
}