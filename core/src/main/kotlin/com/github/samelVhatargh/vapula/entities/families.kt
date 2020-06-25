package com.github.samelVhatargh.vapula.entities

import com.badlogic.ashley.core.Family
import com.github.samelVhatargh.vapula.components.Dead
import com.github.samelVhatargh.vapula.components.Graphics
import com.github.samelVhatargh.vapula.components.OccupySpace
import com.github.samelVhatargh.vapula.components.Position
import ktx.ashley.allOf
import ktx.ashley.exclude

/**
 * Все сущности которые могут отображаться на карте
 */
val RENDERABLE_FAMILY: Family by lazy { allOf(Position::class, Graphics::class).get() }

/**
 * Все сущности которые занимают тайл на карте
 */
val OCCUPY_SPACE_FAMILY: Family by lazy { allOf(Position::class, OccupySpace::class).exclude(Dead::class).get() }