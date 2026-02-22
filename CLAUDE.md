# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Trivia application built with Clean Architecture, MVVM, Jetpack Compose, Hilt dependency injection, Room database, and Retrofit for API calls.

## Architecture

The app follows Clean Architecture with three distinct layers:

### 1. Presentation Layer (UI)
- Jetpack Compose for all UI components
- ViewModels with StateFlow for reactive state management
- MVVM pattern with separation of concerns
- Navigation using Jetpack Compose Navigation

### 2. Domain Layer (Business Logic)
- Domain models (Question, Category) shared between layers
- Repository interface defining the contract
- Use cases containing pure business logic
- Dependency injection setup

### 3. Data Layer (Infrastructure)
- Room database for local caching (questions and favorites)
- Retrofit API service for fetching questions from Open Trivia DB
- Repository implementation handling data sources
- Hilt DI modules for providing dependencies

### Key Architectural Patterns

**Repository Pattern**: Data layer implements `TriviaRepository` interface defined in domain layer. Implementation (`TriviaRepositoryImpl`) orchestrates between API calls and database operations.

**Dependency Injection**: Uses Hilt with `@Singleton` scope for most dependencies. Data layer uses `@Binds` to inject implementation, domain layer uses `@Provides` to expose use cases.

**Clean Architecture**: Each layer has its own directory and is independent. Domain layer should not depend on data/presentation layers.

## Build Commands

### Building
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build artifacts
./gradlew clean

# Build with Gradle daemon enabled
./gradlew build --build-cache
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run instrumented tests on connected device/emulator
./gradlew connectedAndroidTest

# Run tests and generate coverage report
./gradlew testDebugUnitTest jacocoTestReport
```

### Dependency Management
```bash
# List all Gradle tasks
./gradlew tasks

# Refresh dependencies
./gradlew build --refresh-dependencies

# View dependency tree
./gradlew app:dependencies
```

## Project Structure

```
TriviaApp/
├── app/src/main/java/com/neo/trivia/
│   ├── data/                          # Data layer
│   │   ├── api/                       # Retrofit API definitions
│   │   │   ├── ApiConstants.kt       # Base URL and constants
│   │   │   ├── Category.kt           # Category enum matching API
│   │   │   ├── QuestionResponse.kt   # API response models
│   │   │   └── TriviaApi.kt          # Retrofit interface
│   │   ├── database/                  # Room database
│   │   │   ├── entity/               # Room entities
│   │   │   │   ├── QuestionEntity.kt
│   │   │   │   └── FavoriteEntity.kt
│   │   │   ├── dao/                  # Data Access Objects
│   │   │   │   ├── QuestionDao.kt
│   │   │   │   └── FavoriteDao.kt
│   │   │   └── TriviaDatabase.kt     # Room database singleton
│   │   ├── model/                     # Data layer models
│   │   │   └── QuestionModel.kt
│   │   ├── repository/                # Repository implementations
│   │   │   └── TriviaRepositoryImpl.kt
│   │   └── di/                        # Data layer DI module
│   │       └── DataModule.kt
│   ├── domain/                        # Domain layer
│   │   ├── model/                     # Domain models
│   │   │   ├── Category.kt
│   │   │   └── Question.kt
│   │   ├── repository/                # Repository interface
│   │   │   └── TriviaRepository.kt
│   │   ├── usecase/                   # Business logic
│   │   │   ├── GetQuestionsUseCase.kt
│   │   │   ├── GetFavoriteQuestionsUseCase.kt
│   │   │   ├── ToggleFavoriteUseCase.kt
│   │   │   ├── GetStatisticsUseCase.kt
│   │   │   └── ClearHistoryUseCase.kt
│   │   └── di/                        # Domain layer DI module
│   │       └── DomainModule.kt
│   ├── ui/                            # Presentation layer
│   │   ├── Navigation.kt             # NavHost setup
│   │   ├── trivia/                    # Trivia screen
│   │   │   ├── TriviaScreen.kt
│   │   │   └── TriviaViewModel.kt
│   │   ├── favorites/                 # Favorites screen
│   │   │   ├── FavoritesScreen.kt
│   │   │   └── FavoritesViewModel.kt
│   │   ├── stats/                     # Statistics screen
│   │   │   ├── StatisticsScreen.kt
│   │   │   └── StatisticsViewModel.kt
│   │   ├── components/                # Reusable UI components
│   │   ├── theme/                     # Theme configuration
│   │   └── MainActivity.kt            # App entry point
│   └── TriviaApp.kt                   # @HiltAndroidApp
├── gradle/libs.versions.toml          # Dependency versions
├── settings.gradle.kts
├── build.gradle.kts                   # Project-level build config
└── app/build.gradle.kts               # Module-level build config
```

## Development Guidelines

### Adding New Features

**Use Cases**: Create new use case in `domain/usecase/`, add DI provider in `domain/di/DomainModule.kt`, inject into ViewModel.

**Repository**: Implement repository methods in `data/repository/TriviaRepositoryImpl.kt`. Update `TriviaRepository` interface in domain layer.

**UI Components**: Add Compose screens in `ui/` subdirectories with corresponding ViewModels.

**Navigation**: Add new routes in `ui/Navigation.kt` composable.

### Data Layer Changes

- Entities go in `data/database/entity/`
- DAOs go in `data/database/dao/`
- Database class in `data/database/TriviaDatabase.kt`
- Converters for type conversion in `data/database/Converters.kt`

### API Integration

The app uses Open Trivia DB API (https://opentdb.com/api.php). API calls are handled via `TriviaApi` interface in `data/api/TriviaApi.kt`. Add new API endpoints there with appropriate models.

## Dependencies

Key libraries (see `gradle/libs.versions.toml` for versions):
- Hilt 2.59.2 - Dependency injection
- Room 2.8.4 - Local database
- Retrofit 3.0.0 - REST API client
- OkHttp 5.3.2 - HTTP client with logging
- Jetpack Compose BOM 2024.06.00 - UI toolkit
- Kotlin 2.3.10 - Language

## Testing

- Unit tests in `app/src/test/java/`
- Instrumented tests in `app/src/androidTest/java/`

## API Reference

Open Trivia DB API:
- Base URL: `https://opentdb.com/api.php`
- Category filtering via `category` parameter (use Category enum values)
- Question count via `amount` parameter
- No API key required for basic usage

## Key Implementation Notes

1. **Data Caching**: Questions fetched from API are automatically cached in Room database
2. **Flow**: Repository methods returning database queries return Flow for reactive updates
3. **Hilt Navigation Compose**: Uses Hilt for ViewModel injection in Navigation composable
4. **ProGuard**: Configured for release builds (see `app/proguard-rules.pro`)