# Trivia App

A complete Trivia application built with Clean Architecture, MVVM, Jetpack Compose, Hilt, Room, Retrofit, and Kotlin Coroutines Flow.

## Features

- 🎯 Fetch trivia questions from Open Trivia DB API
- 📱 Beautiful Jetpack Compose UI with Material Design 3
- 🔐 Hilt Dependency Injection
- 💾 Room Database for local caching
- 🌐 Retrofit for API calls
- 🏷️ Multiple trivia categories
- 🔢 Customizable question count (1-50)
- 🎚️ Three difficulty levels (Easy, Medium, Hard)
- ⭐ Favorite questions feature
- 📊 Quiz history and statistics tracking
- 🔄 Offline support with cached data
- 🎨 Modern 4-color theme system with dark mode
- 💬 Result review with detailed question analysis

## Architecture

The app follows Clean Architecture principles with three main layers:

### 1. Presentation Layer (UI)
- Jetpack Compose UI components
- ViewModels with StateFlow
- MVVM pattern

### 2. Domain Layer (Business Logic)
- Domain models (Question, Category, QuizResult)
- Use cases (GetQuestions, ToggleFavorite, SaveQuizResult, etc.)
- Repository interface

### 3. Data Layer (Infrastructure)
- **Local Data Source**: Room database for caching questions, favorites, and quiz results
  - `QuestionEntity` for storing trivia questions
  - `FavoriteEntity` for storing favorite question IDs
  - `QuizResultEntity` for storing quiz results with individual answer breakdowns
  - `QuestionDao`, `FavoriteDao`, `QuizResultDao` for data access operations
  - `LocalDataSource` interface and implementation for high-level operations
- **Remote Data Source**: Retrofit API service for fetching questions from Open Trivia DB
  - `RemoteDataSource` and `IRemoteDataSource` interfaces
  - `TriviaApi` for API communication
- **Repository Implementation**: Orchestrates between local and remote data sources
- **Result Wrapper**: `Result` sealed class for handling API and database operations
- **Use Cases**: Business logic layer (SaveQuizResultUseCase, etc.)
- Hilt DI modules for providing dependencies

## Dependencies

- **Hilt**: Dependency Injection
- **Room**: Local database
- **Retrofit**: API client
- **OkHttp**: HTTP client with logging
- **Kotlin Coroutines**: Asynchronous programming
- **Flow**: Reactive data streams
- **Jetpack Compose**: UI toolkit
- **Material3**: Material Design 3
- **Timber**: Logging library
- **Jetpack Navigation Compose**: Navigation

## Architecture Highlights

### Data Layer Split
The data layer is now split into separate modules:
- **Local Data Source**: Handles all local database operations (insert, query, update, delete)
- **Remote Data Source**: Handles all API communication with Open Trivia DB

This separation allows for:
- Easier testing of data sources
- Better error handling with `Result` wrapper
- Flexibility to swap data sources in the future
- Clearer separation of concerns

### Result Wrapper Pattern
The `Result` sealed class provides a consistent way to handle API and database operations:
- `Result.Success<T>`: Operation completed successfully
- `Result.Loading`: Operation is in progress
- `Result.Failure<Exception>`: Operation failed with error

Extension functions `catchToFlow()` and `mapData()` make working with Results more convenient.

## Project Structure

```
TriviaApp/
├── app/
│   ├── src/main/java/com/neo/trivia/
│   │   ├── data/
│   │   │   ├── api/                    # Retrofit API definitions
│   │   │   │   ├── ApiConstants.kt     # Base URL and constants
│   │   │   │   ├── Category.kt         # Category enum matching API
│   │   │   │   ├── QuestionResponse.kt # API response models
│   │   │   │   └── TriviaApi.kt        # Retrofit interface
│   │   │   ├── database/               # Room database entities & DAOs
│   │   │   │   ├── entity/             # Room entities
│   │   │   │   │   ├── QuestionEntity.kt
│   │   │   │   │   ├── FavoriteEntity.kt
│   │   │   │   │   └── QuizResultEntity.kt
│   │   │   │   ├── dao/                # Data Access Objects
│   │   │   │   │   ├── QuestionDao.kt
│   │   │   │   │   ├── FavoriteDao.kt
│   │   │   │   │   └── QuizResultDao.kt
│   │   │   │   └── TriviaDatabase.kt   # Room database singleton (version 4)
│   │   │   ├── local/                  # Local data source implementation
│   │   │   │   ├── LocalDataSource.kt           # Interface
│   │   │   │   └── LocalDataSourceImpl.kt       # Implementation
│   │   │   ├── remote/                 # Remote data source implementation
│   │   │   │   ├── IRemoteDataSource.kt         # Interface
│   │   │   │   └── RemoteDataSource.kt          # Implementation
│   │   │   ├── Result.kt               # Result wrapper for error handling
│   │   │   ├── repository/             # Repository implementation
│   │   │   │   └── TriviaRepositoryImpl.kt
│   │   │   ├── di/                     # Data layer DI modules
│   │   │   │   ├── DataModule.kt
│   │   │   │   ├── DatabaseModule.kt
│   │   │   │   └── TriviaRepositoryModule.kt
│   │   │   └── model/                  # Data models
│   │   │       └── QuestionModel.kt
│   │   ├── domain/
│   │   │   ├── di/                     # Domain layer DI module
│   │   │   │   └── DomainModule.kt
│   │   │   ├── model/                  # Domain models
│   │   │   │   ├── Category.kt
│   │   │   │   ├── Question.kt
│   │   │   │   ├── Difficulty.kt       # EASY, MEDIUM, HARD
│   │   │   │   └── QuizResult.kt       # Quiz result model
│   │   │   ├── repository/             # Repository interface
│   │   │   │   └── TriviaRepository.kt
│   │   │   └── usecase/                # Business logic use cases
│   │   │       ├── GetCategoriesUseCase.kt
│   │   │       ├── GetQuestionsUseCase.kt
│   │   │       ├── SaveQuizResultUseCase.kt
│   │   │       ├── ToggleFavoriteUseCase.kt
│   │   │       └── ...
│   │   ├── ui/
│   │   │   ├── Navigation.kt           # NavHost setup
│   │   │   ├── trivia/                 # Trivia screens
│   │   │   │   ├── CategorySelectionScreen.kt # Category selection
│   │   │   │   ├── QuestionScreen.kt   # Question display
│   │   │   │   ├── QuizResultScreen.kt # Results display
│   │   │   │   ├── CategorySelectionViewModel.kt
│   │   │   │   ├── QuestionViewModel.kt
│   │   │   │   └── QuizResultViewModel.kt
│   │   │   ├── favorites/              # Favorites screen
│   │   │   │   ├── FavoritesScreen.kt
│   │   │   │   └── FavoritesViewModel.kt
│   │   │   ├── stats/                  # Statistics screen
│   │   │   │   ├── StatisticsScreen.kt
│   │   │   │   └── StatisticsViewModel.kt
│   │   │   ├── settings/               # Settings screen
│   │   │   │   └── SettingsScreen.kt
│   │   │   ├── components/             # Reusable UI components
│   │   │   ├── theme/                  # Theme configuration
│   │   │   └── MainActivity.kt         # App entry point
│   │   └── TriviaApp.kt                # @HiltAndroidApp
│   └── ...
├── design/
│   ├── src/main/java/com/neo/design/
│   │   ├── buttons/                    # Design system buttons
│   │   │   ├── Button.kt
│   │   │   └── LoadingButton.kt
│   │   ├── cards/                      # Design system cards
│   │   │   └── CategoryCard.kt
│   │   └── icons/                      # Design system icons
│   │       └── Icons.kt
│   └── README.md                       # Design system documentation
├── gradle/libs.versions.toml           # Dependency versions
├── settings.gradle.kts
├── build.gradle.kts                    # Project-level build config
└── app/build.gradle.kts                # Module-level build config
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

The app uses Room database for caching questions, favorites, and quiz results locally with the following tables:

- `questions`: Stores trivia questions
  - `id`: Unique question identifier
  - `question`: Question text
  - `correctAnswer`: Correct answer text
  - `incorrectAnswers`: List of incorrect answers
  - `category`: Category ID
  - `type`: Question type (multiple choice, etc.)
  - `difficulty`: Difficulty level

- `favorites`: Stores favorite question IDs
  - `questionId`: Reference to question in `questions` table

- `quiz_results`: Stores quiz results with detailed breakdown
  - `id`: Unique result identifier
  - `timestamp`: Quiz completion timestamp (used for ordering)
  - `categoryName`: Category name
  - `categoryIcon`: Category icon
  - `score`: Total correct answers
  - `totalQuestions`: Total questions answered
  - `questionsJson`: JSON array of all questions (for review)
  - `quizResultsJson`: JSON array of individual answer results

## Usage

1. **Start Quiz**: Select a category and difficulty level, then tap "Start Quiz"
2. **Answer Questions**: Select an answer for each question (1-50 questions)
3. **Review Results**: See detailed quiz results with:
   - Final score and percentage
   - Question-by-question breakdown
   - Correct vs incorrect answers
   - Category information
4. **Quiz History**: View all previous quiz results with detailed reviews
5. **Favorites**: Save questions to favorites from the trivia screen
6. **Statistics**: View your quiz history and performance stats
7. **Settings**: Customize app settings (theme, etc.)

**Quiz Result Features:**
- Results are automatically saved after each quiz completion
- Up to 10 recent results are retained in local database
- Results include full question review with original answers
- History persists across app sessions

## Development

### Adding a new category

1. Add to `Category` enum in both data and domain layers
2. The API will handle category filtering automatically

### Adding a new use case

1. Create use case in `domain/usecase/`
2. Add DI provider in `domain/di/DomainModule.kt`
3. Use in ViewModel

### Adding a new data source method

1. Add method to `IRemoteDataSource` interface
2. Implement in `RemoteDataSource` in `data/remote/`
3. Update `TriviaRepositoryImpl` to use the new method
4. Add DI provider in `DataModule.kt`

### Adding a new database table

1. Create entity in `data/database/entity/`
2. Create DAO in `data/database/dao/`
3. Update `TriviaDatabase.kt` to include the DAO and update version number
4. Create use case if needed for business logic
5. Add method to `LocalDataSource` interface and implementation if needed

## Testing

The app includes:
- Unit tests for use cases and repository
- Instrumented tests for ViewModels and UI

## Known Limitations

- Real-time multiplayer via Firebase is not yet implemented
- Pass-and-play mode is not yet implemented
- Online multiplayer is not yet implemented
- Question sharing functionality is a TODO
- Search functionality through saved questions is not yet implemented

## Future Enhancements

- [ ] Real-time multiplayer via Firebase
- [ ] Pass-and-play mode
- [ ] Online multiplayer
- [ ] More question types (boolean, text input)
- [ ] User profiles
- [ ] Leaderboards
- [ ] Difficulty selection on question selection
- [ ] Daily challenges
- [ ] Sound effects and music
- [ ] Achievement system with badges
- [ ] Dark/light theme toggle
- [ ] Multiple theme customization options
- [ ] Question sharing via social media
- [ ] Progress persistence across app sessions
- [ ] Offline question cache refresh

## License

This project is for educational purposes.

## Credits

- Open Trivia DB API: https://opentdb.com/
- Jetpack Compose
- Material Design 3
- Hilt
- Room
- Retrofit
- Architecture documentation in `ARCHITECTURE.md`

## Design System

The app includes a separate `design` module with reusable UI components, typography, and icons:

- **Typography**: Material3-based typography with custom tokens
- **Icons**: Comprehensive icon set with Trivia-specific icons
- **Buttons**: Multiple button variants (Primary, Secondary, Tertiary, Outlined, Loading)
- **Cards**: Base card and specialized cards (StatCard, CategoryCard)
- **Design Tokens**: Consistent spacing and corner radius values

See `design/README.md` for complete design system documentation.

## Theme System

The app features a 4-color theme system with dark mode support:

- **Primary**: Main app color (used for primary actions and highlights)
- **Secondary**: Secondary color (used for secondary actions)
- **Tertiary**: Tertiary color (used for tertiary actions)
- **Quaternary**: Quaternary color (used for special emphasis)

The theme automatically adapts between light and dark modes based on system preferences.