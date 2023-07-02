package com.github.samelVhatargh.vapula.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.github.samelVhatargh.vapula.addModalDialogWindow
import com.github.samelVhatargh.vapula.ui.CustomDialogButton
import com.github.samelVhatargh.vapula.game.statuses.Dead
import ktx.ashley.allOf

/**
 * Manages game over state and conditions
 */
class GameOverSystem: IteratingSystem(allOf(PlayerComponent::class, Dead::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        engine.addModalDialogWindow("Game Over", "You are dead", arrayOf(
            CustomDialogButton("Exit") { Gdx.app.exit(); },
        ))
        setProcessing(false)
    }
}