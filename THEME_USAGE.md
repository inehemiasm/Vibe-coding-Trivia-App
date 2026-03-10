# Theme Usage Guide 🎨

## Overview

The Trivia App uses a custom Material 3 based theme system with **5 distinct modes**. The design tokens are centralized in the `:design` module to ensure consistency across the main app and any future libraries.

## 🎨 Available Theme Modes

1.  **Playful** (Default): Deep Purple & Amber. Built for a classic "Game Show" feel.
2.  **Vibrant**: Energetic Orange & Blue.
3.  **Ocean**: Calm Teal & Blue.
4.  **Sunset**: Warm Purple & Pink.
5.  **Mint**: Fresh Green & Teal.

All themes automatically support **Dark Mode** with optimized contrast ratios.

## 🛠️ Implementation Details

### Theme Configuration
Located in: `app/src/main/java/com/neo/trivia/ui/theme/Theme.kt`

The `TriviaAppTheme` composable takes a `ThemeMode` enum:
```kotlin
@Composable
fun TriviaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeMode: ThemeMode = ThemeMode.Playful,
    content: @Composable () -> Unit
)
```

### Accessing Colors in UI
Always use `MaterialTheme.colorScheme` to ensure your UI adapts to the selected theme mode:
```kotlin
Text(
    text = "Correct!",
    color = MaterialTheme.colorScheme.primary // Adapts to Purple, Orange, etc.
)
```

## 🏗️ Design System Components (:design module)

### 1. Buttons
- `PrimaryButton`: Full-width, high emphasis.
- `SecondaryButton`: Medium emphasis.
- `TertiaryButton`: Low emphasis.
- `OutlinedButton`: Bordered button.

### 2. Cards
- `AppCard`: Base card with standard elevation and corner radius.
- `CategoryCard`: Square card with icon and selection state support.
- `StatCard`: Large icon and value display for statistics.

### 3. Tokens
- **Colors**: Defined in `TriviaColors.kt`.
- **Spacing**: Consistent padding and margin values.
- **CornerRadius**: Standardized rounding (SM: 8dp, MD: 12dp, LG: 16dp, XL: 20dp).

## 🚀 Adding a New Theme

1.  **Define Colors**: Add a new `ColorScheme` object in `TriviaColors.kt` (design module).
2.  **Register Mode**: Add the new mode to the `ThemeMode` enum in `Theme.kt`.
3.  **Map Colors**: Update the `when` block in `TriviaAppTheme` to map the new mode to your colors.
4.  **Update Settings**: Ensure the settings screen allows users to select the new theme.
