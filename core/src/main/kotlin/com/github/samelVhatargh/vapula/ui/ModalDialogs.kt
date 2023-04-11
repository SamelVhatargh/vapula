package com.github.samelVhatargh.vapula.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.samelVhatargh.vapula.components.ModalDialog
import com.github.samelVhatargh.vapula.components.ModalDialogButton
import ktx.actors.onClick
import ktx.app.KtxInputAdapter
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.scene2d.*

/**
 * Manages dialog windows
 */
class ModalDialogs(private val inputMultiplexer: InputMultiplexer) : IteratingSystem(allOf(ModalDialog::class).get()),
    KtxInputAdapter {

    private var stage: Stage? = null

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        stage?.act()
        stage?.draw()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (stage == null) {
            val modalDialog = entity[ModalDialog.mapper]!!
            show(modalDialog.title, modalDialog.text, modalDialog.buttons)
            engine.removeEntity(entity)
        }
    }

    private fun show(title: String, text: String, buttons: Array<ModalDialogButton>) {
        var buttonContainer: KTableWidget
        val dialog = scene2d.table {
            table {
                label(title, LabelStyle.CAPTION.name)
                row()
                label(text) {
                    wrap = true
                    it.minWidth(8 * 64f).pad(16f)
                }
                row()
                buttonContainer = table {
                    defaults().pad(8f)
                }
                background("modal")
            }

            setFillParent(true)
        }

        buttons.forEach { button ->
            buttonContainer.add(scene2d.button {
                label(button.text())
                onClick { button.action() }
            })
        }

        stage = Stage(FitViewport(16 * 64f, 9 * 64f))
        stage!!.addActor(dialog)
        inputMultiplexer.addProcessor(0, this)
        inputMultiplexer.addProcessor(0, stage)
    }

    fun closeModal() {
        inputMultiplexer.removeProcessor(this)
        inputMultiplexer.removeProcessor(stage)
        stage?.dispose()
        stage = null
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = true
    override fun keyDown(keycode: Int) = true
    override fun keyTyped(character: Char) = true
    override fun keyUp(keycode: Int) = true
    override fun mouseMoved(screenX: Int, screenY: Int) = true
    override fun scrolled(amountX: Float, amountY: Float) = true
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = true
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = true
}