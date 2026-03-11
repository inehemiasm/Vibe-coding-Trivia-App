# CLAUDE.md - Development Guide

## Build & Run Commands
- Build project: `./gradlew build`
- Run app: `./gradlew installDebug` (or use Android Studio Run button)
- Clean project: `./gradlew clean`
- Run unit tests: `./gradlew test`
- Run instrumented tests: `./gradlew connectedAndroidTest`
- Refresh dependencies: `./gradlew build --refresh-dependencies`

## Architecture & Layers
This project follows **Clean Architecture** with **MVI (Model-View-Intent)**:
- **Presentation (UI)**: Jetpack Compose, ViewModels (extending `BaseViewModel`), StateFlow.
- **Domain**: Use Cases, Repository interfaces, Domain models (`Question`, `Category`).
- **Data**: Retrofit (Remote), Room (Local), Repository implementations.

## AI Integration
- **Model**: `gemini-2.5-flash-lite` (or latest available) via Google AI SDK.
- **Manager**: `TriviaAiManager` handles prompts for hints and answer explanations.
- **Security**: `GEMINI_API_KEY` is injected via global `gradle.properties` into `BuildConfig`.
- **Connectivity**: AI features are disabled when the `NetworkMonitor` detects an offline state.

## Code Style & Guidelines
- **Language**: Kotlin 1.9+
- **Concurrency**: Coroutines & Flow (StateFlow for UI state).
- **DI**: Hilt (`@Inject`, `@Module`, `@InstallIn`).
- **UI**: 100% Jetpack Compose with Material 3.
- **Offline First**:
    - Repository bypasses remote fetch if `NetworkMonitor` reports offline.
    - Periodic background sync (weekly) via WorkManager (`SyncQuestionsWorker`).
- **Error Handling**: Use the `Result` sealed class for data operations.

## Design System
- Resides in the `:design` module.
- Uses **Tokens** for colors, spacing, and corner radius.
- Theme configuration is in `app/src/main/java/com/neo/trivia/ui/theme/Theme.kt`.

## Common Tasks
- **Add a Screen**: Create Composable, ViewModel, and add to `NavHost` in `Navigation.kt`.
- **Modify AI Prompts**: Update logic in `TriviaAiManager.kt`.
- **Toggle Background Sync**: Update `SyncPreferencesManager.kt` or Settings screen switch.
