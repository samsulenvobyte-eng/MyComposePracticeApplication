package com.example.mypracticeapplication.presentation.ttboost_animation

import androidx.compose.ui.graphics.Color

/**
 * TtBoost Animation Theme
 * Centralized color palette and theme constants
 */
object TtBoostTheme {
    // Background
    val DarkBackground = Color(0xFF0B0F19)
    
    // Bar Chart Colors
    val BarTopColor = Color(0xFFA86E90)
    val BarBottomColor = Color(0xFF0B0F19)
    val BarGradient = listOf(BarTopColor, BarBottomColor)
    
    // Stat Bubble Colors
    object Bubble {
        val HeartColor = Color(0xFFE84E66)
        val HeartShadow = Color(0xFFA62C41)
        val PersonColor = Color(0xFF2DB3F9)
        val PersonShadow = Color(0xFF1A8BD4)
    }
}


