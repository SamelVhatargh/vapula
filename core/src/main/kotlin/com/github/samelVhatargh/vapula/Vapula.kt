package com.github.samelVhatargh.vapula

import ktx.app.KtxGame
import ktx.app.KtxScreen

class Vapula : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}