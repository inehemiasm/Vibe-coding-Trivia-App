# Trivia App: Architecture & Development Guide 🏆

This document outlines the architectural principles, patterns, and best practices used in the Trivia App to ensure it is robust, offline-capable, and easy to maintain.

---

## 1. Clean Architecture + MVI

The app is built using **Clean Architecture** with a strict separation of concerns across three layers:
- **Data Layer**: Retrofit for Trivia APIs, Room for local caching, RSS Feed parsing for Dev Hub, and a Repository to orchestrate them.
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

## 3. Core Modules & ViewModels

### 1. Trivia Module
- **CategoryViewModel**: Manages category selection and weekly WorkManager sync (20+ questions per category).
- **QuestionViewModel**: Manages the quiz session. Features **AI-Powered Hints** using Gemini 2.5 Flash Lite.
- **QuizResultViewModel**: Handles scoring and **AI-Powered Explanations** for incorrect answers.

### 2. Dev Hub Module
- **DevHubViewModel**: Manages the news aggregator. Integrates RSS feeds from Android/iOS sources.
- **Features**: 
    - **Questions Tab**: View and manage saved/favorite trivia questions.
    - **Discover Tab**: Real-time RSS feed browsing.
    - **Internal WebView**: Custom browser with AdBlocker and navigation control to keep content within the app.

---

## 4. AI & Generative Features 🤖

The app leverages **Google AI SDK (Gemini)** to enhance the learning experience.
- **Model**: `gemini-2.5-flash-lite`.
- **Connectivity Logic**: AI features (Hints & Explanations) are gracefully disabled when the device is offline.

---

## 5. Data Layer & Offline-First Strategy 📶

- **Room Persistence**: Acting as the single source of truth for Trivia and Dev Hub metadata.
- **Background Sync**: WorkManager periodic sync (weekly) for fresh trivia content.
- **Internal WebView**: Optimized to prevent external browser launches, ensuring the user stays within the app ecosystem.

---

## 6. Internationalization (i18n)

- **Strings.xml**: All UI strings are extracted to `strings.xml` to support future localization and ensure consistency.
- **Formatted Strings**: Used for dynamic content like "Question X of Y" or "Score: %d".

---

## 7. UI & Design System 🎨

- **Centralized Tokens**: Colors, spacing, and radii defined in the `:design` module. 
- **Dynamic Theming**: Support for 5 custom themes and system dark mode.
- **Type-Safe Navigation**: Jetpack Compose Navigation using Kotlin Serialization for route definitions.
