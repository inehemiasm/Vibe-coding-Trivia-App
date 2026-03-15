# CLAUDE.md - Development Guide

## Build & Run Commands
- Build project: `./gradlew build`
- Run app: `./gradlew installDebug` (or use Android Studio Run button)
- Clean project: `./gradlew clean`
- Run unit tests: `./gradlew test`
- Run instrumented tests: `./gradlew connectedAndroidTest`

## Architecture & Layers
This project follows **Clean Architecture** with **MVI (Model-View-Intent)**:
- **Presentation (UI)**: Jetpack Compose, ViewModels (extending `BaseViewModel`), StateFlow.
- **Domain**: Use Cases, Repository interfaces, Domain models (`Question`, `Category`, `MediumPost`).
- **Data**: Retrofit (Remote), Room (Local), RSS Feed parsing for Dev Hub, Repository implementations.

## Key Modules
- **Trivia**: Question engine with Open Trivia DB and WorkManager-based weekly sync.
- **Dev Hub**: News aggregator (RSS) with an internal AdBlocker-enabled WebView.
- **Design System**: A shared `:design` module containing custom tokens, buttons, and cards.

## AI Integration
- **Model**: `gemini-2.5-flash-lite` via Google AI Kotlin SDK.
- **Manager**: `TriviaAiManager` handles prompts for hints and answer explanations.
- **Connectivity**: AI features are disabled when the `NetworkMonitor` detects an offline state.

## Navigation & i18n
- **Type-Safe Navigation**: Navigation Compose using Kotlin Serialization for routes.
- **Internationalization**: All UI strings must be stored in `strings.xml` using `stringResource()`. Avoid hardcoded strings in Composables.

## Code Style & Guidelines
- **Language**: Kotlin 2.0+
- **Concurrency**: Coroutines & Flow (StateFlow for UI state).
- **DI**: Hilt (`@Inject`, `@Module`, `@InstallIn`).
- **UI**: 100% Jetpack Compose with Material 3.
- **Offline First**:
    - Repository bypasses remote fetch if `NetworkMonitor` reports offline.
    - Persistent storage for trivia, stats, and Dev Hub metadata.

## Common Tasks
- **Add a Screen**: Create Composable, ViewModel, and add to `NavHost` in `Navigation.kt`.
- **Add String Resource**: Update `app/src/main/res/values/strings.xml` and use `R.string.key`.
- **Modify WebView Behavior**: Update `AdBlockWebViewClient` in `WebViewScreen.kt`.
- **Update AI Prompts**: Update logic in `TriviaAiManager.kt`.
