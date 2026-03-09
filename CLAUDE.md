# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Trivia application built with Clean Architecture, MVI (Model-View-Intent), Jetpack Compose, Hilt dependency injection, Room database, and Retrofit for API calls.

## Architecture

The app follows Clean Architecture with three distinct layers:

### 1. Presentation Layer (UI)
- Jetpack Compose for all UI components
- **MVI Pattern**: ViewModels extend `BaseViewModel` with `UiState`, `UiIntent`, and `UiEffect`.
- Navigation using Jetpack Compose Navigation with type-safe routes.

### 2. Domain Layer (Business Logic)
- Domain models (Question, Category) shared between layers.
- Repository interface defining the contract.
- Use cases containing pure business logic.

### 3. Data Layer (Infrastructure)
- Room database for local caching and quiz history.
- Retrofit API service for fetching questions from Open Trivia DB.
- Repository implementation orchestrating between data sources.

### Key Architectural Patterns

**MVI Architecture**: Every screen has a single state object (`UiState`). User actions are sent as `UiIntent`. One-time events (navigation, snackbars) are handled via `UiEffect`.

**Repository Pattern**: Data layer implements the repository interface defined in the domain layer.

**Dependency Injection**: Uses Hilt for managing dependencies across all layers.

## Build Commands

### Building
```bash
./gradlew assembleDebug       # Build debug APK
./gradlew clean               # Clean build artifacts
./gradlew build --build-cache # Build with cache
```

### Testing
```bash
./gradlew test                        # Run unit tests
./gradlew connectedAndroidTest         # Run instrumented tests
```

## Project Structure (Key Paths)

- `app/src/main/java/com/neo/trivia/core/`: Base MVI classes (`BaseViewModel`, etc.)
- `app/src/main/java/com/neo/trivia/ui/`: Presentation layer (Screens and ViewModels)
- `app/src/main/java/com/neo/trivia/domain/`: Domain layer (Models, Use Cases, Repository interfaces)
- `app/src/main/java/com/neo/trivia/data/`: Data layer (API, Database, Repository implementations)
- `design/`: Design system module (Shared UI components)

## Development Guidelines

### Adding New Features

1. **State & Intent**: Define `UiState`, `UiIntent`, and `UiEffect` in the ViewModel file.
2. **ViewModel**: Extend `BaseViewModel`, implement `handleIntent(intent: UiIntent)`.
3. **Screen**: Observe `uiState` using `collectAsStateWithLifecycle()`. Send intents using `viewModel.onIntent(Intent)`.
4. **Effects**: Use `LaunchedEffect` to observe `viewModel.effect`.
5. **Use Case**: Add new business logic to `domain/usecase/` and provide it in `DomainModule`.

### MVI Example

#### ViewModel Implementation
```kotlin
class MyViewModel : BaseViewModel<MyState, MyIntent, MyEffect>(MyState()) {
    override suspend fun handleIntent(intent: MyIntent) {
        when (intent) {
            is MyIntent.Submit -> {
                // ... logic
                sendEffect(MyEffect.NavigateToSuccess)
            }
        }
    }
}
```

#### Composable Implementation
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Observing Effects
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MyEffect.NavigateToSuccess -> { /* Navigate */ }
                is MyEffect.ShowError -> { /* Show Snackbar */ }
            }
        }
    }

    // Sending Intents
    Button(onClick = { viewModel.onIntent(MyIntent.Submit) }) {
        Text("Submit")
    }
}
```

## API Reference

Open Trivia DB API:
- Base URL: `https://opentdb.com/api.php`
- Category filtering via `category` parameter.
- No API key required.

## Key Implementation Notes

1. **Unidirectional Data Flow**: State flows down to UI, Intents flow up to ViewModel.
2. **State Immutability**: Always update state using `setState { copy(...) }`.
3. **Side Effects**: `UiEffect` should be used for events that aren't part of the persistent UI state (navigation, single-use alerts).
4. **Reactive Streams**: Use `Flow` for database queries and API results.
5. **Design System**: Use components from the `:design` module for UI consistency.
