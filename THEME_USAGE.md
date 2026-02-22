# Theme Usage Guide

The Trivia App now supports 4 beautiful color themes with full dark mode support.

## Available Themes

1. **Vibrant** (Default) - Orange + Blue (energetic, fun)
2. **Ocean** - Teal + Blue (fresh, modern)
3. **Sunset** - Purple + Orange (warm, engaging)
4. **Mint** - Green + Teal (fresh, playful)

## Usage

### Switching Themes

You can change the theme by modifying the `themeMode` parameter in `TriviaAppTheme()`:

```kotlin
@Composable
fun MyApp() {
    TriviaAppTheme(
        darkTheme = isSystemInDarkTheme(),
        themeMode = ThemeMode.Sunset,  // Choose: Vibrant, Ocean, Sunset, or Mint
        dynamicColor = true,
        content = {
            // Your app content here
        }
    )
}
```

### Theme Switching in App

To let users switch themes, you can add a theme picker:

```kotlin
@Composable
fun ThemePicker() {
    val currentTheme = remember { mutableStateOf(ThemeMode.Vibrant) }

    Column {
        currentTheme.value = when (selectedOption) {
            "Vibrant" -> ThemeMode.Vibrant
            "Ocean" -> ThemeMode.Ocean
            "Sunset" -> ThemeMode.Sunset
            "Mint" -> ThemeMode.Mint
        }
    }
}
```

### Complete Example

```kotlin
@Composable
fun MainActivity() {
    val darkTheme = isSystemInDarkTheme()
    val systemTheme = remember { mutableStateOf(ThemeMode.Vibrant) }

    TriviaAppTheme(
        darkTheme = darkTheme,
        themeMode = systemTheme.value,
        dynamicColor = true,
        content = {
            Navigation()
        }
    )
}
```

## Theme Details

Each theme includes:
- **Primary Color** - Main brand color for buttons, accents
- **Secondary Color** - Supporting color for secondary UI elements
- **Tertiary Color** - Additional accent color for special highlights
- **Background** - App background color
- **Surface** - Card and panel background colors
- **SurfaceVariant** - Secondary surface colors
- **Error** - Error state color
- **Dark Mode** - Proper dark theme variants for each theme

## Default Settings

The app defaults to:
- **Vibrant** theme
- Light mode (follows system preference)
- Dynamic color enabled (Android 12+)

## Custom Themes

To create a custom theme, you can create additional color schemes in `design/src/main/java/com/neo/design/tokens/colors.kt` by extending the `ColorScheme` data class.