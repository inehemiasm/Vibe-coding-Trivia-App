# Trivia App Design System

A comprehensive design system module for the Trivia App, providing consistent UI components, typography, and icons across all screens.

## Overview

This module follows Clean Architecture principles and provides reusable UI components that can be easily maintained and extended. It separates design concerns from business logic, following the single responsibility principle.

## Module Structure

```
design/src/main/java/com/neo/design/
├── typography/
│   ├── Typography.kt           # Compose Typography object
│   └── tokens.kt               # Typography design tokens
├── icons/
│   ├── Icons.kt                # Icon set definitions
│   ├── Icon.kt                 # Icon wrapper component
│   └── sizes.kt                # Icon size constants
├── buttons/
│   ├── Button.kt               # Base button component
│   ├── PrimaryButton.kt        # Primary variant
│   ├── SecondaryButton.kt      # Secondary variant
│   ├── TertiaryButton.kt       # Tertiary variant
│   ├── OutlinedButton.kt       # Outline variant
│   └── LoadingButton.kt        # Button with loading state
├── cards/
│   ├── Card.kt                 # Base card component
│   └── StatCard.kt             # Statistics card component
└── tokens/
    ├── spacing.kt              # Spacing design tokens
    └── cornerRadius.kt         # Corner radius tokens
```

## Typography

### Using Typography

The design system provides a Material3 typography system with expanded styles:

```kotlin
import com.neo.design.typography.Typography

// Use in MaterialTheme
MaterialTheme(
    typography = Typography,
    content = content
)
```

### Typography Tokens

Available typography tokens for programmatic usage:

```kotlin
import com.neo.design.typography.TypographyToken

TypographyToken.H1  // 32sp, Bold
TypographyToken.H2  // 28sp, Bold
TypographyToken.H3  // 24sp, Medium
TypographyToken.T1  // 20sp, Normal
TypographyToken.T2  // 16sp, Normal (Body)
TypographyToken.T3  // 14sp, Normal (Small)
TypographyToken.B1  // 16sp, Bold
TypographyToken.B2  // 14sp, Bold (Small)
TypographyToken.B3  // 12sp, Medium (Label)
TypographyToken.L1  // 13sp, Medium (Caption)
TypographyToken.L2  // 11sp, Normal (Caption Small)
TypographyToken.C1  // 10sp, Normal (Caption Extra Small)
```

## Icon System

### Using Icons

The design system provides a comprehensive icon set and wrapper component:

```kotlin
import com.neo.design.icons.TriviaIcons
import com.neo.design.icons.AppIcon
import com.neo.design.icons.IconSize

// Use with design system icons
AppIcon(
    icon = TriviaIcons.Home,
    contentDescription = "Home",
    size = IconSize.Large,
    tint = MaterialTheme.colorScheme.primary
)

// Navigation icons
TriviaIcons.Home
TriviaIcons.Back
TriviaIcons.Close
TriviaIcons.Search
TriviaIcons.Settings

// Action icons
TriviaIcons.Favorite
TriviaIcons.FavoriteBorder
TriviaIcons.Info
TriviaIcons.Check
TriviaIcons.Error

// Quiz icons
TriviaIcons.Play
TriviaIcons.Pause
TriviaIcons.Replay
TriviaIcons.Next
TriviaIcons.Previous

// UI icons
TriviaIcons.More
TriviaIcons.Expand
TriviaIcons.Collapse
```

### Icon Sizes

```kotlin
IconSize.Small    // 16.dp
IconSize.Medium   // 20.dp
IconSize.Large    // 24.dp
IconSize.XLarge   // 32.dp
IconSize.XXLarge  // 48.dp
```

## Button System

### Base Button

```kotlin
import com.neo.design.buttons.TriviaButton
import com.neo.design.buttons.ButtonStyle

TriviaButton(
    text = "Click me",
    onClick = { /* your action */ },
    modifier = Modifier.fillMaxWidth(),
    enabled = true,
    style = ButtonStyle.Primary,
    loading = false,
    icon = TriviaIcons.Play
)
```

### Button Variants

```kotlin
import com.neo.design.buttons.PrimaryButton
import com.neo.design.buttons.SecondaryButton
import com.neo.design.buttons.TertiaryButton
import com.neo.design.buttons.OutlinedButton

// Primary - Main actions (filled primary color)
PrimaryButton(
    text = "Start Quiz",
    onClick = { /* your action */ }
)

// Secondary - Secondary actions (filled secondary color)
SecondaryButton(
    text = "Save",
    onClick = { /* your action */ }
)

// Tertiary - Tertiary actions (filled tertiary color)
TertiaryButton(
    text = "Cancel",
    onClick = { /* your action */ }
)

// Outlined - Outline variant (bordered)
OutlinedButton(
    text = "Cancel",
    onClick = { /* your action */ }
)
```

### Loading Button

```kotlin
import com.neo.design.buttons.LoadingButton

LoadingButton(
    text = "Loading...",
    onClick = { /* your action */ },
    enabled = false
)
```

## Card System

### Base Card

```kotlin
import com.neo.design.cards.AppCard

AppCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = { /* your action */ },
    horizontalPadding = 16.dp,
    verticalPadding = 16.dp
) {
    Text("Card content")
}
```

### Stat Card

```kotlin
import com.neo.design.cards.StatCard

StatCard(
    icon = TriviaIcons.Info,
    title = "Total Questions",
    value = "42",
    color = MaterialTheme.colorScheme.primary
)
```

## Design Tokens

### Spacing

```kotlin
import com.neo.design.tokens.Spacing

Spacing.xs  // 4.dp
Spacing.sm  // 8.dp
Spacing.md  // 16.dp
Spacing.lg  // 24.dp
Spacing.xl  // 32.dp
Spacing.xxl // 48.dp
```

### Corner Radius

```kotlin
import com.neo.design.tokens.CornerRadius

CornerRadius.sm  // 8.dp
CornerRadius.md  // 12.dp
CornerRadius.lg  // 16.dp
CornerRadius.xl  // 20.dp
```

## Integration with App Module

The design module is automatically integrated with the app module. To use design system components:

1. Import from design module:
   ```kotlin
   import com.neo.design.buttons.PrimaryButton
   import com.neo.design.cards.AppCard
   import com.neo.design.icons.AppIcon
   import com.neo.design.typography.Typography
   ```

2. Use components directly in your screens

3. Update `Theme.kt` to use design typography:
   ```kotlin
   MaterialTheme(
       typography = com.neo.design.typography.Typography,
       content = content
   )
   ```

## Best Practices

1. **Consistency**: Always use design system components for UI elements
2. **Typography**: Use appropriate typography tokens based on content hierarchy
3. **Icons**: Use TriviaIcons instead of raw Material icons for consistency
4. **Spacing**: Use Spacing tokens for consistent spacing between elements
5. **Buttons**: Choose the appropriate button variant based on the action's importance
6. **Dark/Light Mode**: Design system components automatically adapt to the current theme

## Extending the Design System

### Adding New Icons

Add new icons to `TriviaIcons` object in `icons/Icons.kt`:

```kotlin
object TriviaIcons {
    // ... existing icons

    // Add your new icon
    val NewIcon = Icons.Default.YourIcon
}
```

### Adding New Button Styles

Extend `ButtonStyle` enum and add corresponding component in `buttons/Button.kt`.

### Adding New Typography Styles

Add new tokens to `TypographyToken` enum in `typography/tokens.kt`.

## Dependencies

- Android Gradle Plugin: 9.0.1
- Kotlin: 2.3.10
- Compose BOM: 2024.06.00
- Material3

## License

Part of the Trivia App project.