package com.github.samelVhatargh.vapula.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.samelVhatargh.vapula.Vapula

fun main() {
    Lwjgl3Application(
        Vapula(),
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("Vapula")
            setWindowedMode(16 * 64, 9 * 64)
            setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        }
    )
}

