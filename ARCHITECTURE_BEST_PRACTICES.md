# Trivia App: Architecture & Best Practices 🏆

This document outlines the core architectural decisions and coding standards that make the Trivia App robust, offline-capable, and easy to maintain.

## 1. Clean Architecture + MVI

We use a strict separation of concerns across three layers:
- **Data**: Retrofit for API, Room for local caching, and a Repository to orchestrate them.
- **Domain**: Pure Kotlin business logic (Use Cases) and interfaces.
- **Presentation**: Jetpack Compose and ViewModels following the **MVI (Model-View-Intent)** pattern.

### Why MVI?
Unidirectional Data Flow (UDF) ensures that the UI state is predictable. Every change in the UI is a result of a specific `UiIntent` being processed by the ViewModel.

## 2. Offline-First Strategy 📶

The app is built to be fully functional without an internet connection.

### Implementation:
- **Proactive Sync**: The `SyncQuestionsUseCase` runs in the background when categories are loaded, downloading 20 questions per category.
- **Hybrid Repository**: The repository always attempts a network fetch first. If it fails (or if the user is offline), it seamlessly falls back to the local Room database.
- **Smart Filtering**: The `CategoryViewModel` observes the local database and filters the home screen to show **only** categories that have cached questions when the user is offline.

## 3. Data Integrity & Persistence 💾

### Avoiding ID Collisions
To prevent different quiz sessions from overwriting each other in the database, we generate unique IDs for questions based on a hash of their text:
```kotlin
id = apiQuestion.question.hashCode().toString()
```

### SQL Priority Fallback
Our `QuestionDao` uses a priority-based SQL query. It prioritizes the selected category but will "fill up" the quiz to 10 questions using random data from other categories if the chosen one is short on local data.

## 4. UI & Design System 🎨

### Centralized Tokens
All colors, spacing, and corner radii are defined as tokens in the `:design` module. Never hardcode colors in the `:app` module; always use `MaterialTheme.colorScheme`.

### Square Category Cards
For a consistent grid, category cards are enforced to a 1:1 aspect ratio. This ensures a clean, professional look regardless of the category name length.

## 5. Coding Standards

- **Compose**: Use `collectAsStateWithLifecycle()` to observe ViewModels safely.
- **DI**: Always use Hilt for dependency injection. Interface-based injection for Repositories and DataSources is mandatory for testing.
- **Navigation**: Use type-safe routes in `Navigation.kt`. Always handle the backstack carefully (e.g., using `popUpTo` with `inclusive = true` when finishing a quiz).
- **Icons**: Prefer `Icons.AutoMirrored` where applicable (e.g., Back arrows) to support Right-to-Left (RTL) languages.

## 6. Future-Proofing

- **Scalable Themes**: The `ThemeMode` enum makes it trivial to add new color schemes.
- **Modular Design**: The separation of the `:design` module allows for the design system to be shared with other potential apps (e.g., a Trivia Creator app).
