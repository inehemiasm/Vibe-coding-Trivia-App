# Trivia App: Architecture & Development Guide 🏆

This document outlines the architectural principles, patterns, and best practices used in the Trivia App to ensure it is robust, offline-capable, and easy to maintain.

---

## 1. Clean Architecture + MVI

The app is built using **Clean Architecture** with a strict separation of concerns across three layers:
- **Data Layer**: Retrofit for API, Room for local caching, Google AI SDK (Gemini) for hints, and a Repository to orchestrate them.
- **Domain Layer**: Pure Kotlin business logic (Use Cases) and repository interfaces.
- **Presentation Layer**: Jetpack Compose and ViewModels following the **MVI (Model-View-Intent)** pattern.

---

## 2. Presentation Layer (MVI)

### BaseViewModel
All ViewModels inherit from `BaseViewModel<S : UiState, I : UiIntent, E : UiEffect>`.
- **UiState**: Represents the entire state of the screen at any given time.
- **UiIntent**: User actions or events that trigger a state change.
- **UiEffect**: One-time side effects like navigation or showing a snackbar.

---

## 3. Current ViewModels

### 1. CategoryViewModel
**Purpose:** Manages the category selection screen (`CategorySelectionScreen`).
**Responsibilities:**
- Load available quiz categories from the use case.
- **Optimized Weekly Sync**: Uses WorkManager to download 20+ questions per category once a week, ensuring data freshness while minimizing network usage.
**Location**: `app/src/main/java/com/neo/trivia/ui/trivia/CategoryViewModel.kt`

### 2. QuestionViewModel
**Purpose:** Manages the active quiz session (`QuestionScreen`).
**Responsibilities:**
- Load questions based on selected category and difficulty.
- **AI-Powered Hints**: Integrates Gemini 2.5 Flash Lite to provide context-aware, subtle hints. Automatically hidden when offline to prevent errors.
- **Connectivity Awareness**: Observes real-time network status via `NetworkMonitor` to adapt features dynamically.
**Location**: `app/src/main/java/com/neo/trivia/ui/trivia/QuestionViewModel.kt`

### 3. QuizResultViewModel
**Purpose:** Manages results and history.
**Responsibilities:**
- **On-Demand AI Explanations**: Allows users to click "Explain why with AI" for any reviewed question, fetching a concise Gemini-powered explanation.
**Location**: `app/src/main/java/com/neo/trivia/ui/trivia/QuizResultViewModel.kt`

---

## 4. AI & Generative Features 🤖

The app leverages **Google AI SDK (Gemini)** to enhance the learning experience.

### Implementation:
- **Model**: `gemini-2.5-flash-lite` for its superior balance of speed, cost, and intelligence.
- **Security**: The `GEMINI_API_KEY` is managed via a global `gradle.properties` file and injected into `BuildConfig`.
- **Connectivity Logic**: AI features are gracefully disabled when the device is offline to maintain a smooth UX.

---

## 5. Data Layer & Offline-First Strategy 📶

The app is designed to be fully functional without an internet connection.

### Implementation:
- **Network Monitoring**: A `NetworkMonitor` utility provides a real-time Flow of connectivity status.
- **Intelligent Routing**: The Repository detects offline status and immediately bypasses remote calls to avoid unnecessary latency.
- **Background Sync (WorkManager)**: A periodic `SyncQuestionsWorker` runs weekly (when online and charging) to refresh the local cache.
- **Persistence**: Room database acts as the single source of truth for offline play.

---

## 6. UI & Design System 🎨

- **Centralized Tokens**: Colors, spacing, and corner radii defined in the `:design` module. 
- **Dynamic Theming**: Support for 5 custom themes and system dark mode.
- **Responsive Layouts**: Screens use `verticalScroll` and optimized padding to ensure usability on small devices.

---

## 7. Navigation & Coding Standards

- **Type-Safe Navigation**: Jetpack Compose Navigation with type-safe routes.
- **Dependency Injection**: 100% Hilt-based, including Hilt Work for WorkManager injection.
- **Compose Best Practices**: Use `collectAsStateWithLifecycle()` for safe state observation.
