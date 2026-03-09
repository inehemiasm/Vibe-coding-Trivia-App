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
│   ├── CategoryCard.kt         # Card for category selection
│   ├── StatCard.kt             # Statistics card component
│   └── AppCard.kt              # General purpose app card
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

## Card System

### Base Card

```kotlin
import com.neo.design.cards.AppCard

AppCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = { /* your action */ }
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

### Category Card

```kotlin
import com.neo.design.cards.CategoryCard

CategoryCard(
    categoryName = "Science",
    onCategoryClick = { /* select category */ }
)
```

## UI State Management (MVI Integration)

While the `design` module is primarily for UI components, these components are designed to work seamlessly with the app's **MVI (Model-View-Intent)** architecture.

- **Stateless Components**: All design system components are stateless and rely on parameters passed from the UI layer.
- **Predictable Updates**: Components respond to changes in the `UiState` emitted by ViewModels.
- **Intent Handling**: Click listeners in components (like `onClick`) are used to trigger `UiIntent` calls in the presentation layer.

## Integration with App Module

The design module is automatically integrated with the app module. To use design system components:

1. Import from design module:
   ```kotlin
   import com.neo.design.buttons.PrimaryButton
   import com.neo.design.cards.AppCard
   import com.neo.design.icons.AppIcon
   ```

2. Use components directly in your screens.

3. Update `Theme.kt` to use design typography:
   ```kotlin
   MaterialTheme(
       typography = com.neo.design.typography.Typography,
       content = content
   )
   ```

## Best Practices

1. **Consistency**: Always use design system components for UI elements.
2. **Typography**: Use appropriate typography tokens based on content hierarchy.
3. **Icons**: Use `TriviaIcons` instead of raw Material icons for consistency.
4. **Spacing**: Use `Spacing` tokens for consistent spacing between elements.
5. **Buttons**: Choose the appropriate button variant based on the action's importance.
6. **Dark/Light Mode**: Design system components automatically adapt to the current theme.

## Dependencies

- Android Gradle Plugin: 9.0.1
- Kotlin: 2.3.10
- Compose BOM: 2024.06.00
- Material3

## License

Part of the Trivia App project.
