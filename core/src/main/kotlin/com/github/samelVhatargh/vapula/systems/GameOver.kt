package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.samelVhatargh.vapula.addModalDialogWindow
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Player
import ktx.ashley.allOf

/**
 * Manages game over state and conditions
 */
class GameOver: IteratingSystem(allOf(Player::class, Dead::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        engine.addModalDialogWindow("Game Over", "You are dead")
        setProcessing(false)
    }
}