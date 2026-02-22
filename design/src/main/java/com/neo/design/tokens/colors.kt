package com.neo.design.tokens

import androidx.compose.ui.graphics.Color

/**
 * Trivia App Color Palette
 *
 * Theme Options:
 * - Vibrant: Orange + Blue (energetic, fun)
 * - Ocean: Teal + Blue (fresh, modern)
 * - Sunset: Purple + Orange (warm, engaging)
 * - Mint: Green + Teal (fresh, playful)
 */

data class ColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val error: Color,
    val onError: Color
)

object TriviaColors {
    // Vibrant Theme (Default)
    val Vibrant = ColorScheme(
        primary = Color(0xFFFF6B35),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF0077B6),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFF4CC9F0),
        onTertiary = Color(0xFF0B1026),
        background = Color(0xFFFFFBF4),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF0F4F8),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF)
    )

    // Vibrant Dark Theme
    val VibrantDark = ColorScheme(
        primary = Color(0xFFFF6B35),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF0077B6),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFF4CC9F0),
        onTertiary = Color(0xFF0B1026),
        background = Color(0xFF0F172A),
        surface = Color(0xFF1E293B),
        surfaceVariant = Color(0xFF334155),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF)
    )

    // Ocean Theme
    val Ocean = ColorScheme(
        primary = Color(0xFF0EA5E9),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF0284C7),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFF6366F1),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFF0F9FF),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFE0F2FE),
        error = Color(0xFFEF4444),
        onError = Color(0xFFFFFFFF)
    )

    // Ocean Dark Theme
    val OceanDark = ColorScheme(
        primary = Color(0xFF0EA5E9),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF0284C7),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFF6366F1),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFF0C4A6E),
        surface = Color(0xFF0369A1),
        surfaceVariant = Color(0xFF075985),
        error = Color(0xFFEF4444),
        onError = Color(0xFFFFFFFF)
    )

    // Sunset Theme
    val Sunset = ColorScheme(
        primary = Color(0xFF8B5CF6),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFFF97316),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFFEC4899),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFFDF4FF),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFFCE7F3),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF)
    )

    // Sunset Dark Theme
    val SunsetDark = ColorScheme(
        primary = Color(0xFF8B5CF6),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFFF97316),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFFEC4899),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFF3B0764),
        surface = Color(0xFF6D28D9),
        surfaceVariant = Color(0xFF581C87),
        error = Color(0xFFDC2626),
        onError = Color(0xFFFFFFFF)
    )

    // Mint Theme
    val Mint = ColorScheme(
        primary = Color(0xFF10B981),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF06B6D4),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFF8B5CF6),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFF0FDF4),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFDCFCE7),
        error = Color(0xFFEF4444),
        onError = Color(0xFFFFFFFF)
    )

    // Mint Dark Theme
    val MintDark = ColorScheme(
        primary = Color(0xFF10B981),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF06B6D4),
        onSecondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFF8B5CF6),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFF064E3B),
        surface = Color(0xFF059669),
        surfaceVariant = Color(0xFF047857),
        error = Color(0xFFEF4444),
        onError = Color(0xFFFFFFFF)
    )
}