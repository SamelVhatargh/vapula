package com.github.samelVhatargh.vapula.entities

import com.badlogic.ashley.core.Family
import com.github.samelVhatargh.vapula.game.statuses.DeadComponent
import com.github.samelVhatargh.vapula.game.statuses.OccupySpaceComponent
import com.github.samelVhatargh.vapula.graphics.AnimationComponent
import com.github.samelVhatargh.vapula.graphics.GraphicsComponent
import com.github.samelVhatargh.vapula.map.PositionComponent
import ktx.ashley.allOf
import ktx.ashley.exclude

/**
 * Entities which must be rendered on screen
 */
val RENDERABLE_FAMILY: Family by lazy { allOf(PositionComponent::class, GraphicsComponent::class).get() }

/**
 * Entities which can be animated
 */
val ANIMATION_FAMILY: Family by lazy { allOf(PositionComponent::class, GraphicsComponent::class, AnimationComponent::class).get() }

/**
 * Entities which can be moved onto
 */
val OCCUPY_SPACE_FAMILY: Family by lazy { allOf(PositionComponent::class, OccupySpaceComponent::class).exclude(DeadComponent::class).get() }