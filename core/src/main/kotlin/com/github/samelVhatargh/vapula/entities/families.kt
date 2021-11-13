package com.github.samelVhatargh.vapula.entities

import com.badlogic.ashley.core.Family
import com.github.samelVhatargh.vapula.components.*
import ktx.ashley.allOf
import ktx.ashley.exclude

/**
 * Entities which must be rendered on screen
 */
val RENDERABLE_FAMILY: Family by lazy { allOf(Position::class, Graphics::class).get() }

/**
 * Entities which can be animated
 */
val ANIMATION_FAMILY: Family by lazy { allOf(Position::class, Graphics::class, Animation::class).get() }

/**
 * Entities which can be moved onto
 */
val OCCUPY_SPACE_FAMILY: Family by lazy { allOf(Position::class, OccupySpace::class).exclude(Dead::class).get() }