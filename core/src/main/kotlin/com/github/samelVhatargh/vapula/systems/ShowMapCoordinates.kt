package com.github.samelVhatargh.vapula.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import ktx.app.KtxInputAdapter
import ktx.math.vec3

class ShowMapCoordinates(private val camera: Camera, private val inputMultiplexer: InputMultiplexer) :
    EntitySystem(), KtxInputAdapter {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        inputMultiplexer.addProcessor(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        inputMultiplexer.removeProcessor(this)
    }

    var x = 0
    var y = 0

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        Gdx.graphics.setTitle("$x;$y")
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val position = camera.unproject(vec3(screenX.toFloat(), screenY.toFloat()))
        x = position.x.toInt()
        y = position.y.toInt()

        return false
    }
}