# Trivia App

A complete Trivia application built with Clean Architecture, MVVM, Jetpack Compose, Hilt, Room, and Retrofit.

## Features

- 🎯 Fetch trivia questions from Open Trivia DB API
- 📱 Beautiful Jetpack Compose UI
- 🔐 Hilt Dependency Injection
- 💾 Room Database for caching
- 🌐 Retrofit for API calls
- 🏷️ Multiple trivia categories
- 🔢 Customizable question count (1-50)
- ⭐ Favorite questions feature
- 📊 Statistics tracking
- 🔄 Offline support with cached data
- 🎨 Modern Material Design 3

## Architecture

The app follows Clean Architecture principles with three main layers:

### 1. Presentation Layer (UI)
- Jetpack Compose UI components
- ViewModels with StateFlow
- MVVM pattern

### 2. Domain Layer (Business Logic)
- Domain models (Question, Category)
- Use cases (GetQuestions, ToggleFavorite, etc.)
- Repository interface

### 3. Data Layer (Infrastructure)
- Room database for local caching
- Retrofit API service
- Repository implementation
- Hilt DI modules

## Dependencies

- **Hilt**: Dependency Injection
- **Room**: Local database
- **Retrofit**: API client
- **OkHttp**: HTTP client with logging
- **Kotlin Coroutines**: Asynchronous programming
- **Jetpack Compose**: UI toolkit
- **Material3**: Material Design 3

## Project Structure

```
TriviaApp/
├── app/
│   ├── src/main/java/com/neo/trivia/
│   │   ├── data/
│   │   │   ├── api/              # Retrofit API definitions
│   │   │   ├── database/         # Room database entities & DAOs
│   │   │   ├── di/               # Data layer DI
│   │   │   ├── model/            # Data models
│   │   │   └── repository/       # Repository implementation
│   │   ├── domain/
│   │   │   ├── di/               # Domain layer DI
│   │   │   ├── model/            # Domain models
│   │   │   ├── repository/       # Repository interface
│   │   │   └── usecase/          # Business logic use cases
│   │   ├── ui/
│   │   │   ├── components/       # Reusable UI components
│   │   │   ├── favorites/        # Favorites screen
│   │   │   ├── navigation/       # Navigation setup
│   │   │   ├── stats/            # Statistics screen
│   │   │   ├── theme/            # Theme configuration
│   │   │   └── trivia/           # Trivia screen
│   │   └── MainActivity.kt
│   └── ...
└── ...
```

## Getting Started

### Prerequisites

- Android Studio Koala (2023.1.1) or later
- JDK 11 or higher
- Gradle 8.0 or higher

### Installation

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build the project

## Configuration

### API Configuration

The app uses Open Trivia DB API:
- Base URL: `https://opentdb.com/api.php`
- No API key required for basic usage

### Database

The app uses Room database for caching questions locally with the following tables:
- `questions`: Stores trivia questions
- `favorites`: Stores favorite question IDs

## Usage

1. **Start Quiz**: Select a category and number of questions, then tap "Start Quiz"
2. **Answer Questions**: Select an answer for each question
3. **View Results**: See your score at the end
4. **Favorites**: Save questions to favorites from the trivia screen
5. **Statistics**: View your question history and stats
6. **Search**: Search through saved questions (feature pending)

## Development

### Adding a new category

1. Add to `Category` enum in both data and domain layers
2. The API will handle category filtering automatically

### Adding a new use case

1. Create use case in `domain/usecase/`
2. Add DI provider in `domain/di/DomainModule.kt`
3. Use in ViewModel

## Testing

The app includes:
- Unit tests for use cases and repository
- Instrumented tests for ViewModels and UI

## Known Limitations

- Multiplayer feature is planned but not implemented in this MVP
- Some screens are still in progress

## Future Enhancements

- [ ] Real-time multiplayer via Firebase
- [ ] Pass-and-play mode
- [ ] Online multiplayer
- [ ] More question types
- [ ] User profiles
- [ ] Leaderboards
- [ ] Difficulty levels
- [ ] Daily challenges
- [ ] Sound effects
- [ ] Achievement system

## License

This project is for educational purposes.

## Credits

- Open Trivia DB API: https://opentdb.com/
- Jetpack Compose
- Material Design 3
- Hilt
- Room
- Retrofit