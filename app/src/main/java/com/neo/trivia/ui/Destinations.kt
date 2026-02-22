package com.neo.trivia.ui

import com.neo.trivia.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Trivia(val category: Category? = null)

@Serializable
object Stats

@Serializable
object Settings

@Serializable
object Favorites