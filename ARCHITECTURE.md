# Trivia App: Architecture & Development Guide 🏆

This document outlines the architectural principles, patterns, and best practices used in the Trivia App to ensure it is robust, offline-capable, and easy to maintain.

---

## 1. Clean Architecture + MVI

The app is built using **Clean Architecture** with a strict separation of concerns across three layers:
- **Data Layer**: Retrofit for API, Room for local caching, and a Repository to orchestrate them.
- **Domain Layer**: Pure Kotlin business logic (Use Cases) and repository interfaces.
- **Presentation Layer**: Jetpack Compose and ViewModels following the **MVI (Model-View-Intent)** pattern.

### Why MVI?
Unidirectional Data Flow (UDF) ensures that the UI state is predictable. Every change in the UI is a result of a specific `UiIntent` being processed by the ViewModel, leading to a new `UiState`.

---

## 2. Presentation Layer (MVI)

### BaseViewModel
All ViewModels inherit from `BaseViewModel<S : UiState, I : UiIntent, E : UiEffect>`.
- **UiState**: Represents the entire state of the screen at any given time.
- **UiIntent**: User actions or events that trigger a state change.
- **UiEffect**: One-time side effects like navigation or showing a snackbar.

### Data Flow (MVI Pattern)
1. **User Action**: User interacts with the UI (e.g., clicks an answer).
2. **Intent**: The UI calls `viewModel.onIntent(Intent)`.
3. **State Change**: The ViewModel processes the intent in `handleIntent`, updates the `UiState` using `setState { ... }`.
4. **UI Update**: The Compose screen observes `viewModel.uiState` and recomposes automatically.
5. **Side Effect**: If an action requires a one-time event (like navigation), the ViewModel calls `sendEffect(Effect)`.

---

## 3. Current ViewModels

### 1. CategoryViewModel
**Purpose:** Manages the category selection screen (`CategorySelectionScreen`).
**Responsibilities:**
- Load available quiz categories from the use case.
- **Offline Sync**: Automatically triggers `SyncQuestionsUseCase` to download 20+ questions per category for offline play.
- **Reactive Cache**: Observes the local database to show only categories with cached questions when offline.
**MVI Definition:**
- **State**: `CategoryUiState` (loading, syncing status, list of categories, error).
- **Intents**: `LoadCategories`, `SyncOfflineData`.
- **Location**: `app/src/main/java/com/neo/trivia/ui/trivia/CategoryViewModel.kt`

### 2. QuestionViewModel
**Purpose:** Manages the active quiz session (`QuestionScreen`).
**Responsibilities:**
- Load questions based on selected category and difficulty.
- **Hybrid Sourcing**: Fetches from the API but automatically falls back to the local database if offline.
- **Unique Question Hashing**: Generates unique IDs for questions based on a hash of their text to prevent collisions.
- Track progress, score, and individual answer results.
- Save quiz results to the database upon completion.
**MVI Definition:**
- **State**: `QuestionUiState` (questions, current index, score, results, loading/error status).
- **Intents**: `LoadQuestions`, `SelectAnswer`, `ResetQuizState`.
- **Effects**: `NavigateToResults` (triggered when the quiz finishes).
- **Location**: `app/src/main/java/com/neo/trivia/ui/trivia/QuestionViewModel.kt`

### 3. QuizResultViewModel
**Purpose:** Manages the results display and history review (`QuizResultScreen`).
**Responsibilities:**
- Fetch specific quiz results by ID or the latest saved session.
- Provide functionality to reset the quiz state for a retake.
**MVI Definition:**
- **State**: `QuizResultUiState` (final score, results breakdown, loading status).
- **Intents**: `LoadSavedResults`, `LoadResultById`, `ResetQuiz`.
- **Location**: `app/src/main/java/com/neo/trivia/ui/trivia/QuizResultViewModel.kt`

### 4. FavoritesViewModel
**Purpose:** Manages the user's favorite questions (`FavoritesScreen`).
**Responsibilities:**
- Stream favorite questions from the local database.
- Handle toggling the favorite status of a question.
**MVI Definition:**
- **State**: `FavoritesUiState` (list of favorite questions, loading status).
- **Intents**: `LoadFavorites`, `ToggleFavorite`.
- **Location**: `app/src/main/java/com/neo/trivia/ui/favorites/FavoritesViewModel.kt`

### 5. StatisticsViewModel
**Purpose:** Manages the user's overall performance metrics (`StatisticsScreen`).
**Responsibilities:**
- Aggregate data from quiz history to display total questions and performance.
- Provide a list of recent quiz attempts.
**MVI Definition:**
- **State**: `StatisticsUiState` (quiz history list, total question count, loading status).
- **Intents**: `LoadStatistics`.
- **Location**: `app/src/main/java/com/neo/trivia/ui/stats/StatisticsViewModel.kt`

---

## 4. Data Layer & Offline-First Strategy 📶

The app is designed to be fully functional without an internet connection.

### Implementation:
- **Proactive Sync**: The `SyncQuestionsUseCase` runs in the background when categories are loaded, downloading 20+ questions per category.
- **Hybrid Repository**: The repository always attempts a network fetch first. If it fails, it seamlessly falls back to the local Room database.
- **Smart Filtering**: The home screen reactively filters to show only categories that have cached questions when the user is offline.

### Data Integrity & Persistence 💾
- **Unique Question Hashing**: To prevent collisions, we generate unique IDs for questions based on a hash of their text.
- **SQL Priority Fallback**: Our `QuestionDao` uses a priority-based query. It prioritizes the selected category but will "fill up" the quiz to 10 questions using random data from other categories if the chosen one is short on local data.

---

## 5. UI & Design System 🎨

- **Centralized Tokens**: All colors, spacing, and corner radii are defined as tokens in the `:design` module. 
- **Theming**: 5 distinct theme modes with full dark mode support. Always use `MaterialTheme.colorScheme` instead of hardcoded colors.
- **Cohesive Components**: Shared components like `AppCard` and `PrimaryButton` ensure a consistent look and feel.

---

## 6. Navigation & Coding Standards

- **Type-Safe Navigation**: Uses Jetpack Compose Navigation with type-safe routes.
- **Backstack Management**: Quiz screens are popped from the backstack (`inclusive = true`) upon completion so that "Back" from the results screen leads Home.
- **Dependency Injection**: 100% Hilt-based. Interface-based injection is used for Repositories and DataSources to facilitate testing.
- **Compose Best Practices**: Use `collectAsStateWithLifecycle()` for safe state observation.

---

## 7. Key Benefits

1. **Single Source of Truth**: The state is immutable and held in one place.
2. **Predictability**: Every UI change is traceable to a specific intent.
3. **Debuggability**: Clear separation of user actions and state transitions.
4. **Testability**: Business logic is isolated within the `handleIntent` function.
5. **Resilience**: A robust offline fallback ensures the user experience is never interrupted.
