package com.github.samelVhatargh.vapula.systems.commands

/**
 * An action that can be performed by an [entity][com.badlogic.ashley.core.Entity]
 */
interface Command {

    /**
     * Executes the command
     *
     * @return true if this command should be repeated in next turn
     */
    fun execute(): Boolean
}