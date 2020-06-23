package com.github.samelVhatargh.vapula

import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.samelVhatargh.vapula.components.Position

fun Sprite.setPosition(position: Position) {
    setPosition(position.x.toFloat(), position.y.toFloat())
}