package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.Pool
import com.github.samelVhatargh.vapula.ui.ModalDialogs
import ktx.ashley.getSystem
import ktx.ashley.mapperFor

interface ModalDialogButton {
    fun text(): String
    fun action()
}

class CloseDialogButton(val engine: Engine) : ModalDialogButton {
    override fun text(): String = "OK"
    override fun action() = engine.getSystem<ModalDialogs>().closeModal()
}

class CustomDialogButton(private val text: String, private val action: () -> Unit): ModalDialogButton {
    override fun text(): String = text
    override fun action() = action.invoke()
}


class ModalDialog(var title: String = "", var text: String = "", var buttons: Array<ModalDialogButton> = emptyArray()) :
    Component, Pool.Poolable {

    override fun reset() {
        title = ""
        text = ""
        buttons = emptyArray()
    }

    companion object {
        val mapper = mapperFor<ModalDialog>()
    }
}