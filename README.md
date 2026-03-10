# Trivia App 🧠

A complete, modern Trivia application built with **Clean Architecture**, MVVM, Jetpack Compose, Hilt, Room, Retrofit, and Kotlin Coroutines Flow.

## 🚀 Key Features

- **Dynamic Quiz Engine**: Fetch trivia questions from [Open Trivia DB API](https://opentdb.com/api.php).
- **Offline Mode 📶**: Play your favorite categories even without internet.
- **Smart Caching**:
    - Automatically downloads 20+ questions per category for offline play.
    - Intelligent fallback: If you're offline, the app shows only categories with cached questions.
    - SQL-based randomization ensures every offline quiz feels fresh.
- **Modern UI/UX 🎨**:
    - **Playful Theme**: A vibrant Purple & Amber design system built for fun.
    - **Adaptive Icons**: Brand-new custom launcher icon matching the theme.
    - **Responsive Design**: Smooth transitions and layouts optimized for all screen sizes.
- **Advanced Results Review**:
    - Detailed score breakdown with percentage badges.
    - Question-by-question review with color-coded correct/incorrect indicators.
    - Stats summary (Total, Correct, Wrong).
- **Personalization**:
    - **Favorites ⭐**: Save challenging questions to review later.
    - **History 📊**: Track your performance across all past quiz sessions.
- **Theme System**: 5 distinct theme modes (Playful, Vibrant, Ocean, Sunset, Mint) with full Dark Mode support.

## 🏗️ Architecture

The app follows **Clean Architecture** principles, ensuring a scalable and testable codebase:

### 1. Presentation Layer (UI)
- **Jetpack Compose**: 100% declarative UI.
- **StateFlow & ViewModels**: Reactive state management.
- **Navigation Compose**: Type-safe navigation between screens.

### 2. Domain Layer (Business Logic)
- **Use Cases**: Encapsulated logic for syncing questions, fetching categories, and saving results.
- **Repository Interfaces**: Abstracted data operations.

### 3. Data Layer (Infrastructure)
- **Remote**: Retrofit & OkHttp for robust API communication.
- **Local (Room)**: 
    - `QuestionEntity`: Cached trivia questions with unique ID hashing to prevent collisions.
    - `CategoryEntity`: Local cache of trivia categories for offline browsing.
    - `QuizResultEntity`: Persistent history of your quiz performances.
- **Sync Engine**: Background logic that proactively fills the local database with fresh content.

## 🛠️ Tech Stack

- **Kotlin**: Primary language.
- **Jetpack Compose**: Modern UI toolkit.
- **Hilt**: Dependency injection.
- **Room**: Local persistence.
- **Retrofit**: Network communication.
- **Coroutines & Flow**: Asynchronous streams.
- **Timber**: Logging.
- **Material Design 3**: Latest design standards.

## 📁 Project Structure

```
TriviaApp/
├── app/                        # Main Android application module
│   ├── data/                   # API, Room DB, Repository implementations
│   ├── domain/                 # Business logic, Use cases, Repository interfaces
│   └── ui/                     # Compose Screens, ViewModels, Theme
├── design/                     # Shared Design System module
│   ├── tokens/                 # Color schemes, Spacing, Typography
│   ├── buttons/                # Custom button components
│   └── cards/                  # Specialized card components
└── gradle/                     # Version catalogs and build scripts
```

## 🚥 Getting Started

1. **Prerequisites**: Android Studio Ladybug or later.
2. **Setup**:
   - Clone the repository.
   - Sync Gradle.
   - Run the `:app` module on an emulator or physical device.

## 📝 License

This project is for educational purposes.

## 🙌 Credits

- **API**: [Open Trivia DB](https://opentdb.com/)
- **Design**: Built with ❤️ using Jetpack Compose and Material 3.
