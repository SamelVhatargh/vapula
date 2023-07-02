package com.github.samelVhatargh.vapula.ui

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor


class ModalDialogComponent(var title: String = "", var text: String = "", var buttons: Array<ModalDialogButton> = emptyArray()) :
    Component, Pool.Poolable {

    override fun reset() {
        title = ""
        text = ""
        buttons = emptyArray()
    }

    companion object {
        val mapper = mapperFor<ModalDialogComponent>()
    }
}