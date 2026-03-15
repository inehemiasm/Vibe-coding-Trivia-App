# Trivia App 🧠

A complete, modern Trivia application built with **Clean Architecture**, MVVM/MVI, Jetpack Compose, Hilt, Room, Retrofit, and Kotlin Coroutines Flow.

## 🚀 Key Features

- **Dynamic Quiz Engine**: Fetch trivia questions from [Open Trivia DB API](https://opentdb.com/api.php).
- **Offline Mode 📶**: Play your favorite categories even without internet.
- **Smart Caching**:
    - Automatically downloads 20+ questions per category for offline play.
    - Intelligent fallback: If you're offline, the app shows only categories with cached questions.
- **AI-Powered Learning 🤖**: 
    - **Gemini Hints**: Get subtle, context-aware hints during quizzes using Google's Gemini AI.
    - **AI Explanations**: Review your results with on-demand AI explanations for why an answer was correct.
- **Dev Hub 👨‍💻**:
    - Stay updated with the latest Android & iOS news via integrated RSS feeds.
    - **Internal Webview**: Read full articles directly within the app with a built-in AdBlocker.
    - **Offline Snippets**: Save interesting posts to read their summaries later, even without a connection.
- **Modern UI/UX 🎨**:
    - **Multi-Theme System**: 5 distinct theme modes (Playful, Vibrant, Ocean, Sunset, Mint) with full Dark Mode support.
    - **Playful Design**: A vibrant Purple & Amber design system built for fun.
- **Advanced Results Review**:
    - Detailed score breakdown with percentage badges.
    - Question-by-question review with color-coded correct/incorrect indicators.
- **Personalization**:
    - **Favorites ⭐**: Save challenging questions to your Dev Hub for later review.
    - **History 📊**: Track your performance across all past quiz sessions with detailed accuracy stats.

## 🏗️ Architecture

The app follows **Clean Architecture** principles, ensuring a scalable and testable codebase:

### 1. Presentation Layer (UI)
- **Jetpack Compose**: 100% declarative UI.
- **MVI Pattern**: Reactive state management using `UiState`, `UiIntent`, and `UiEffect`.
- **Navigation Compose**: Type-safe navigation between screens.

### 2. Domain Layer (Business Logic)
- **Use Cases**: Encapsulated logic for syncing questions, fetching categories, and managing favorites.
- **Repository Interfaces**: Abstracted data operations for Trivia and Dev Hub content.

### 3. Data Layer (Infrastructure)
- **Remote**: Retrofit & OkHttp for API communication; RSS parsing for Dev Hub.
- **Local (Room)**: Persistent storage for questions, categories, quiz results, and saved Dev Hub posts.
- **AI Integration**: Google AI Kotlin SDK for Gemini-powered features.
- **Sync Engine**: WorkManager-based background logic that proactively refreshes content weekly.

## 🛠️ Tech Stack

- **Kotlin**: Primary language.
- **Jetpack Compose**: Modern UI toolkit.
- **Hilt**: Dependency injection.
- **Room**: Local persistence.
- **Retrofit**: Network communication.
- **Coroutines & Flow**: Asynchronous streams.
- **Google AI SDK**: Gemini 2.5 Flash Lite integration.
- **Coil**: Image loading.
- **Timber**: Logging.

## 📁 Project Structure

```
TriviaApp/
├── app/                        # Main Android application module
│   ├── data/                   # API, RSS, Room DB, Repository implementations
│   ├── domain/                 # Business logic, Use cases, Repository interfaces
│   └── ui/                     # Compose Screens (Trivia, Dev Hub, Settings, Stats), ViewModels, Theme
├── design/                     # Shared Design System module
│   ├── tokens/                 # Color schemes, Spacing, Typography
│   ├── buttons/                # Custom button components
│   └── cards/                  # Specialized card components
└── gradle/                     # Version catalogs and build scripts
```

## 🚥 Getting Started

1. **Prerequisites**: Android Studio Ladybug or later.
2. **AI Setup**: Add your `GEMINI_API_KEY` to `gradle.properties`.
3. **Setup**:
   - Clone the repository.
   - Sync Gradle.
   - Run the `:app` module on an emulator or physical device.

## 📝 License

This project is for educational purposes.

## 🙌 Credits

- **Trivia API**: [Open Trivia DB](https://opentdb.com/)
- **News Feeds**: 9to5Google, Android Developers, Apple News.
- **Design**: Built with ❤️ using Jetpack Compose and Material 3.
