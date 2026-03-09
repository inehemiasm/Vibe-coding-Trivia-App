# ViewModel Architecture

## Principle: Unidirectional Data Flow with MVI

This Trivia app follows the **MVI (Model-View-Intent)** architectural pattern. Each screen has a dedicated ViewModel that extends `BaseViewModel`, ensuring a consistent and predictable state management system.

## Core Components

### BaseViewModel
All ViewModels inherit from `BaseViewModel<S : UiState, I : UiIntent, E : UiEffect>`.
- **UiState**: Represents the entire state of the screen at any given time.
- **UiIntent**: Represents user actions or events that trigger a state change.
- **UiEffect**: Represents one-time side effects like navigation, showing a toast, or playing a sound.

---

## Current ViewModels

### 1. CategoryViewModel
**Purpose:** Manages the category selection screen (`CategorySelectionScreen`).

**Responsibilities:**
- Load available quiz categories from the use case.
- Handle category selection and difficulty configuration.

**MVI Definition:**
- **State**: `CategoryUiState` (loading, list of categories, error message).
- **Intents**: `LoadCategories`.
- **Effects**: None.

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/CategoryViewModel.kt`

---

### 2. QuestionViewModel
**Purpose:** Manages the active quiz session (`QuestionScreen`).

**Responsibilities:**
- Load questions based on selected category and difficulty.
- Track progress, score, and individual answer results.
- Save quiz results to the database upon completion.

**MVI Definition:**
- **State**: `QuestionUiState` (questions, current index, score, results, loading/error status).
- **Intents**: `LoadQuestions`, `SelectAnswer`.
- **Effects**: `NavigateToResults` (triggered when the quiz finishes).

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/QuestionViewModel.kt`

---

### 3. QuizResultViewModel
**Purpose:** Manages the results display and history review (`QuizResultScreen`).

**Responsibilities:**
- Fetch specific quiz results by ID or the latest saved session.
- Provide functionality to reset the quiz state for a retake.

**MVI Definition:**
- **State**: `QuizResultUiState` (final score, results breakdown, loading status).
- **Intents**: `LoadSavedResults`, `LoadResultById`, `ResetQuiz`.
- **Effects**: None.

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/QuizResultViewModel.kt`

---

### 4. FavoritesViewModel
**Purpose:** Manages the user's favorite questions (`FavoritesScreen`).

**Responsibilities:**
- Stream favorite questions from the local database.
- Handle toggling the favorite status of a question.

**MVI Definition:**
- **State**: `FavoritesUiState` (list of favorite questions, loading status).
- **Intents**: `LoadFavorites`, `ToggleFavorite`.
- **Effects**: None.

**Location:** `app/src/main/java/com/neo/trivia/ui/favorites/FavoritesViewModel.kt`

---

### 5. StatisticsViewModel
**Purpose:** Manages the user's overall performance metrics (`StatisticsScreen`).

**Responsibilities:**
- Aggregate data from quiz history to display total questions and performance.
- Provide a list of recent quiz attempts.

**MVI Definition:**
- **State**: `StatisticsUiState` (quiz history list, total question count, loading status).
- **Intents**: `LoadStatistics`.
- **Effects**: None.

**Location:** `app/src/main/java/com/neo/trivia/ui/stats/StatisticsViewModel.kt`

---

## Data Flow (MVI Pattern)

1. **User Action**: User interacts with the UI (e.g., clicks an answer).
2. **Intent**: The UI calls `viewModel.onIntent(Intent)`.
3. **State Change**: The ViewModel processes the intent in `handleIntent`, updates the `UiState` using `setState { ... }`.
4. **UI Update**: The Compose screen observes `viewModel.uiState` and recomposes automatically.
5. **Side Effect**: If an action requires a one-time event (like navigation), the ViewModel calls `sendEffect(Effect)`.

---

## Navigation Pattern

Navigation uses Jetpack Compose Navigation with type-safe routes. When transitioning between screens in a quiz flow, we pass unique identifiers (like `categoryId` or `quizResultId`) and fetch necessary data in the destination ViewModel to ensure state consistency.

---

## Key Benefits of MVI

1. **Single Source of Truth**: The state is immutable and held in one place.
2. **Predictability**: Every UI change is a result of a specific intent.
3. **Debuggability**: Clear separation of user actions and state transitions makes it easier to trace issues.
4. **Testability**: Business logic is isolated within the `handleIntent` function, which can be tested by asserting state changes against specific intents.
