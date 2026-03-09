# Theme Usage Guide

The Trivia App supports 4 beautiful color themes with full dark mode support. Theme preferences are persisted locally using `ThemePreferencesManager`.

## Available Themes

1. **Vibrant** (Default) - Orange + Blue (energetic, fun)
2. **Ocean** - Teal + Blue (fresh, modern)
3. **Sunset** - Purple + Orange (warm, engaging)
4. **Mint** - Green + Teal (fresh, playful)

## Usage

### Theme Provider Setup

In your `MainActivity` or root composable, observe the theme preferences and apply them to `TriviaAppTheme`:

```kotlin
@Composable
fun TriviaApp(
    themePreferencesManager: ThemePreferencesManager
) {
    // Observe theme preferences from DataStore
    val themePrefs by themePreferencesManager.themePreferences.collectAsState(
        initial = ThemePreferences(ThemeMode.Vibrant, false)
    )

    TriviaAppTheme(
        darkTheme = themePrefs.isDarkMode,
        themeMode = themePrefs.themeMode,
        dynamicColor = false // Set to true to use Android 12+ dynamic colors
    ) {
        // App Content (Navigation, etc.)
    }
}
```

### Applying the Theme

The `TriviaAppTheme` composable handles the selection of color schemes based on the provided parameters:

```kotlin
@Composable
fun TriviaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeMode: ThemeMode = ThemeMode.Vibrant,
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
```

## Theme Switching

### Preferences Manager

Theme changes are saved using `ThemePreferencesManager` (DataStore-based):

```kotlin
// Save preferences
themePreferencesManager.saveThemePreferences(ThemeMode.Ocean, true)
```

### Settings Integration

The `SettingsScreen` allows users to toggle dark mode and select their preferred theme. When a user interacts with the UI, the `ThemeIntent` (in an MVI setup) should trigger the update in the `ThemePreferencesManager`.

## Design System Components

All components in the `:design` module are theme-aware and use `MaterialTheme.colorScheme` and `MaterialTheme.typography` to ensure consistency across themes.

- **Primary Actions**: Use `MaterialTheme.colorScheme.primary`
- **Surface Containers**: Use `MaterialTheme.colorScheme.surface` or `surfaceVariant`
- **Text**: Use `MaterialTheme.colorScheme.onSurface` or `onSurfaceVariant`

## Customization

To add a new theme:
1. Define the colors in `design/src/main/java/com/neo/design/tokens/colors.kt`.
2. Add the new mode to the `ThemeMode` enum in `ui/theme/Theme.kt`.
3. Create the corresponding `lightColorScheme` and `darkColorScheme` in `Theme.kt`.
4. Update the `when` expression in `TriviaAppTheme` to include the new theme.
