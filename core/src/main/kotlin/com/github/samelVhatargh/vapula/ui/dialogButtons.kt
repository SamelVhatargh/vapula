package com.github.samelVhatargh.vapula.ui

import com.badlogic.ashley.core.Engine
import ktx.ashley.getSystem

interface ModalDialogButton {
    fun text(): String
    fun action()
}

class CloseDialogButton(val engine: Engine) : ModalDialogButton {
    override fun text(): String = "OK"
    override fun action() = engine.getSystem<ModalDialogSystem>().closeModal()
}

class CustomDialogButton(private val text: String, private val action: () -> Unit): ModalDialogButton {
    override fun text(): String = text
    override fun action() = action.invoke()
}
