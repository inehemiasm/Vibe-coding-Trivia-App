package com.neo.trivia.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.neo.design.typography.Typography as DesignTypography
import com.neo.design.tokens.ColorScheme
import com.neo.design.tokens.TriviaColors

/**
 * Trivia App Theme
 *
 * Theme options: Vibrant, Ocean, Sunset, Mint
 * Can be configured via themeMode parameter
 */
private val VibrantLightColors = lightColorScheme(
    primary = TriviaColors.Vibrant.primary,
    onPrimary = TriviaColors.Vibrant.onPrimary,
    secondary = TriviaColors.Vibrant.secondary,
    onSecondary = TriviaColors.Vibrant.onSecondary,
    tertiary = TriviaColors.Vibrant.tertiary,
    onTertiary = TriviaColors.Vibrant.onTertiary,
    background = TriviaColors.Vibrant.background,
    surface = TriviaColors.Vibrant.surface,
    surfaceVariant = TriviaColors.Vibrant.surfaceVariant,
    error = TriviaColors.Vibrant.error,
    onError = TriviaColors.Vibrant.onError
)

private val VibrantDarkColors = darkColorScheme(
    primary = TriviaColors.VibrantDark.primary,
    onPrimary = TriviaColors.VibrantDark.onPrimary,
    secondary = TriviaColors.VibrantDark.secondary,
    onSecondary = TriviaColors.VibrantDark.onSecondary,
    tertiary = TriviaColors.VibrantDark.tertiary,
    onTertiary = TriviaColors.VibrantDark.onTertiary,
    background = TriviaColors.VibrantDark.background,
    surface = TriviaColors.VibrantDark.surface,
    surfaceVariant = TriviaColors.VibrantDark.surfaceVariant,
    error = TriviaColors.VibrantDark.error,
    onError = TriviaColors.VibrantDark.onError
)

private val OceanLightColors = lightColorScheme(
    primary = TriviaColors.Ocean.primary,
    onPrimary = TriviaColors.Ocean.onPrimary,
    secondary = TriviaColors.Ocean.secondary,
    onSecondary = TriviaColors.Ocean.onSecondary,
    tertiary = TriviaColors.Ocean.tertiary,
    onTertiary = TriviaColors.Ocean.onTertiary,
    background = TriviaColors.Ocean.background,
    surface = TriviaColors.Ocean.surface,
    surfaceVariant = TriviaColors.Ocean.surfaceVariant,
    error = TriviaColors.Ocean.error,
    onError = TriviaColors.Ocean.onError
)

private val OceanDarkColors = darkColorScheme(
    primary = TriviaColors.OceanDark.primary,
    onPrimary = TriviaColors.OceanDark.onPrimary,
    secondary = TriviaColors.OceanDark.secondary,
    onSecondary = TriviaColors.OceanDark.onSecondary,
    tertiary = TriviaColors.OceanDark.tertiary,
    onTertiary = TriviaColors.OceanDark.onTertiary,
    background = TriviaColors.OceanDark.background,
    surface = TriviaColors.OceanDark.surface,
    surfaceVariant = TriviaColors.OceanDark.surfaceVariant,
    error = TriviaColors.OceanDark.error,
    onError = TriviaColors.OceanDark.onError
)

private val SunsetLightColors = lightColorScheme(
    primary = TriviaColors.Sunset.primary,
    onPrimary = TriviaColors.Sunset.onPrimary,
    secondary = TriviaColors.Sunset.secondary,
    onSecondary = TriviaColors.Sunset.onSecondary,
    tertiary = TriviaColors.Sunset.tertiary,
    onTertiary = TriviaColors.Sunset.onTertiary,
    background = TriviaColors.Sunset.background,
    surface = TriviaColors.Sunset.surface,
    surfaceVariant = TriviaColors.Sunset.surfaceVariant,
    error = TriviaColors.Sunset.error,
    onError = TriviaColors.Sunset.onError
)

private val SunsetDarkColors = darkColorScheme(
    primary = TriviaColors.SunsetDark.primary,
    onPrimary = TriviaColors.SunsetDark.onPrimary,
    secondary = TriviaColors.SunsetDark.secondary,
    onSecondary = TriviaColors.SunsetDark.onSecondary,
    tertiary = TriviaColors.SunsetDark.tertiary,
    onTertiary = TriviaColors.SunsetDark.onTertiary,
    background = TriviaColors.SunsetDark.background,
    surface = TriviaColors.SunsetDark.surface,
    surfaceVariant = TriviaColors.SunsetDark.surfaceVariant,
    error = TriviaColors.SunsetDark.error,
    onError = TriviaColors.SunsetDark.onError
)

private val MintLightColors = lightColorScheme(
    primary = TriviaColors.Mint.primary,
    onPrimary = TriviaColors.Mint.onPrimary,
    secondary = TriviaColors.Mint.secondary,
    onSecondary = TriviaColors.Mint.onSecondary,
    tertiary = TriviaColors.Mint.tertiary,
    onTertiary = TriviaColors.Mint.onTertiary,
    background = TriviaColors.Mint.background,
    surface = TriviaColors.Mint.surface,
    surfaceVariant = TriviaColors.Mint.surfaceVariant,
    error = TriviaColors.Mint.error,
    onError = TriviaColors.Mint.onError
)

private val MintDarkColors = darkColorScheme(
    primary = TriviaColors.MintDark.primary,
    onPrimary = TriviaColors.MintDark.onPrimary,
    secondary = TriviaColors.MintDark.secondary,
    onSecondary = TriviaColors.MintDark.onSecondary,
    tertiary = TriviaColors.MintDark.tertiary,
    onTertiary = TriviaColors.MintDark.onTertiary,
    background = TriviaColors.MintDark.background,
    surface = TriviaColors.MintDark.surface,
    surfaceVariant = TriviaColors.MintDark.surfaceVariant,
    error = TriviaColors.MintDark.error,
    onError = TriviaColors.MintDark.onError
)

private val DarkColorScheme = VibrantDarkColors
private val LightColorScheme = VibrantLightColors

enum class ThemeMode {
    Vibrant, Ocean, Sunset, Mint
}

enum class ThemeColor {
    Light, Dark
}

@Composable
fun TriviaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeMode: ThemeMode = ThemeMode.Vibrant,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> when (themeMode) {
            ThemeMode.Vibrant -> VibrantDarkColors
            ThemeMode.Ocean -> OceanDarkColors
            ThemeMode.Sunset -> SunsetDarkColors
            ThemeMode.Mint -> MintDarkColors
        }

        else -> when (themeMode) {
            ThemeMode.Vibrant -> VibrantLightColors
            ThemeMode.Ocean -> OceanLightColors
            ThemeMode.Sunset -> SunsetLightColors
            ThemeMode.Mint -> MintLightColors
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DesignTypography,
        content = content
    )
}