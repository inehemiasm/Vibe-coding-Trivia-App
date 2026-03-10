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

### Current ViewModels
1. **CategoryViewModel**: Manages category selection, handles **Offline Sync** triggers, and reactively filters categories based on cached data.
2. **QuestionViewModel**: Manages the quiz session. Handles **Hybrid Sourcing** (Remote -> Local fallback) and tracks scoring.
3. **QuizResultViewModel**: Displays results and manages history review.
4. **FavoritesViewModel**: Manages favorite questions streamed from the database.
5. **StatisticsViewModel**: Aggregates performance metrics from quiz history.

---

## 3. Data Layer & Offline-First Strategy 📶

The app is designed to be fully functional without an internet connection.

### Implementation:
- **Proactive Sync**: The `SyncQuestionsUseCase` runs in the background when categories are loaded, downloading 20+ questions per category.
- **Hybrid Repository**: The repository always attempts a network fetch first. If it fails, it seamlessly falls back to the local Room database.
- **Smart Filtering**: The home screen reactively filters to show only categories that have cached questions when the user is offline.

### Data Integrity & Persistence 💾
- **Unique Question Hashing**: To prevent collisions, we generate unique IDs for questions based on a hash of their text.
- **SQL Priority Fallback**: Our `QuestionDao` uses a priority-based query. It prioritizes the selected category but will "fill up" the quiz to 10 questions using random data from other categories if the chosen one is short on local data.

---

## 4. UI & Design System 🎨

- **Centralized Tokens**: All colors, spacing, and corner radii are defined as tokens in the `:design` module. 
- **Theming**: 5 distinct theme modes with full dark mode support. Always use `MaterialTheme.colorScheme` instead of hardcoded colors.
- **Cohesive Components**: Shared components like `AppCard` and `PrimaryButton` ensure a consistent look and feel.

---

## 5. Navigation & Coding Standards

- **Type-Safe Navigation**: Uses Jetpack Compose Navigation with type-safe routes.
- **Backstack Management**: Quiz screens are popped from the backstack (`inclusive = true`) upon completion so that "Back" from the results screen leads Home.
- **Dependency Injection**: 100% Hilt-based. Interface-based injection is used for Repositories and DataSources to facilitate testing.
- **Compose Best Practices**: Use `collectAsStateWithLifecycle()` for safe state observation.

---

## 6. Key Benefits

1. **Predictability**: Every UI change is traceable to an intent.
2. **Testability**: Business logic is isolated in Use Cases and ViewModel `handleIntent` blocks.
3. **Resilience**: A robust offline fallback ensures the user experience is never interrupted by connectivity issues.
