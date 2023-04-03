package com.github.samelVhatargh.vapula.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class ModalDialog(var title: String = "", var text: String = "") : Component, Pool.Poolable {

    override fun reset() {
        title = ""
        text = ""
    }

    companion object {
        val mapper = mapperFor<ModalDialog>()
    }
}