# ViewModel Architecture

## Principle: Each Screen Has Its Own ViewModel

This Trivia app follows the principle that **each screen should have its own dedicated ViewModel** with a clear separation of concerns.

## Current ViewModels

### 1. TriviaViewModel
**Purpose:** Manages the home screen (TriviaScreen)

**Responsibilities:**
- Load and manage category list
- Handle user navigation from home to quiz start
- Coordinate with QuestionScreen to start a quiz

**Key State:**
- `categoriesState` - List of available quiz categories
- `uiState` - Current screen state (loading, error, etc.)

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/TriviaViewModel.kt`

---

### 2. QuestionViewModel
**Purpose:** Manages the quiz taking flow (QuestionScreen)

**Responsibilities:**
- Load quiz questions from API
- Track current question index and user answers
- Calculate score during quiz
- Store individual answer results

**Key State:**
- `uiState` - Quiz state (loading, success, error, finished)
- `currentQuestions` - List of questions for current quiz
- `currentQuestionIndex` - Current question being displayed
- `score` - Number of correct answers so far
- `quizResults` - List of all answer results (Question, selected, correct, isCorrect)

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/QuestionViewModel.kt`

---

### 3. QuizResultViewModel
**Purpose:** Manages the quiz results display (QuizResultScreen)

**Responsibilities:**
- Display quiz results to the user
- Show question review with correct/incorrect answers
- Provide action buttons (retake, share)
- Load and display historical quiz results from database
- Save current quiz results to database on completion

**Key State:**
- `uiState` - Quiz state (loading, success, error, finished)
- `currentQuestions` - List of questions for review
- `currentQuestionIndex` - Current question being reviewed
- `score` - Final score
- `quizResults` - List of all answer results for display
- `localDataSource` - Injected for loading historical results
- `saveQuizResultUseCase` - Injected for saving results

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/QuizResultViewModel.kt`

**Initialization:**
- Receives quiz data as parameters from navigation
- Does NOT fetch questions (they come from QuestionScreen)
- Sets initial score and results based on passed data
- Loads historical quiz results from database on initialization

**Data Persistence:**
- Saves completed quiz results with category, score, total questions, and individual answers
- Maintains maximum of 10 recent results using `SaveQuizResultUseCase`
- Results are stored as JSON in `QuizResultEntity`

---

### 4. CategoryViewModel
**Purpose:** Manages category selection screen

**Responsibilities:**
- Load categories from API
- Handle category selection
- Navigate to QuestionScreen with selected category

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/CategoryViewModel.kt`

---

### 5. FavoritesViewModel
**Purpose:** Manages favorites screen

**Responsibilities:**
- Load and display favorite questions
- Handle favorite/unfavorite actions
- Navigate to trivia screen

**Location:** `app/src/main/java/com/neo/trivia/ui/favorites/FavoritesViewModel.kt`

---

### 6. StatisticsViewModel
**Purpose:** Manages statistics screen

**Responsibilities:**
- Load and display quiz statistics
- Calculate success rates
- Handle navigation

**Location:** `app/src/main/java/com/neo/trivia/ui/stats/StatisticsViewModel.kt`

---

### 7. SettingsViewModel
**Purpose:** Manages settings screen

**Responsibilities:**
- Handle settings changes
- Navigate between screens

**Location:** `app/src/main/java/com/neo/trivia/ui/settings/SettingsViewModel.kt`

---

## Shared State: TriviaUiState

**Purpose:** Shared UI state for quiz flow

**Location:** `app/src/main/java/com/neo/trivia/ui/trivia/TriviaUiState.kt`

**State:**
- `Initial` - Initial state before quiz starts
- `Loading` - Loading questions
- `Success` - Questions loaded successfully
- `Error` - Error occurred
- `Finished` - Quiz completed

---

## Data Flow

### Quiz Flow:

1. **User starts quiz** → `TriviaViewModel` triggers navigation to `CategoryScreen`
2. **User selects category** → `CategoryViewModel` navigates to `QuestionScreen`
3. **QuestionScreen loads** → `QuestionViewModel` loads questions
4. **User answers** → `QuestionViewModel` stores results and calculates score
5. **Quiz finished** → `QuestionViewModel` passes data to `QuizResultScreen`
6. **QuizResultScreen displays** → `QuizResultViewModel` loads historical results and shows current results
7. **User retakes** → `QuizResultViewModel.resetQuiz()` clears state

### Quiz Result Persistence:

1. **On Quiz Completion**: `QuestionViewModel.onAnswerSelected()` calls `saveResultToDatabase()`
2. **Save to Database**: `SaveQuizResultUseCase.save()` saves the result with:
   - Category information (name and icon)
   - Score and total questions
   - All questions as JSON
   - All answer results as JSON
3. **Result Limit**: Automatically deletes oldest results if more than 10 exist
4. **History Loading**: `QuizResultViewModel.loadSavedResults()` loads all recent results from database
5. **Screen Display**: Results are displayed in `QuizResultScreen` with score, percentage, and detailed breakdown

**Database Schema:**
- `QuizResultEntity` stores quiz results with `timestamp` for ordering
- Results are ordered by timestamp (newest first) in the database
- Uses Gson for JSON serialization of complex types (Question[], QuizResult[])

---

## Key Benefits

1. **Clear Responsibility:** Each ViewModel has one clear purpose
2. **Easy Testing:** ViewModels are isolated and testable
3. **Maintainability:** Changes to one screen don't affect others
4. **Type Safety:** Shared state types are centralized
5. **Separation of Concerns:** Data fetching, state management, and UI logic are separated

---

## Navigation Pattern

Navigation uses Jetpack Compose Navigation with kotlinx.serialization for complex route parameters.

**Challenge:** Navigation serialization doesn't support `List<T>` types directly.

**Solution:** Use JSON serialization to convert the list to a string and deserialize it in the destination.

```kotlin
@Serializable
data class QuizResultScreen(
    val questionsJson: String,
    val categoryName: String,
    val categoryIcon: String,
    val initialScore: Int
) {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun create(questions: List<Question>, categoryName: String, categoryIcon: String, initialScore: Int): QuizResultScreen {
            return QuizResultScreen(
                questionsJson = json.encodeToString(questions),
                categoryName = categoryName,
                categoryIcon = categoryIcon,
                initialScore = initialScore
            )
        }

        fun getQuestions(data: QuizResultScreen): List<Question> {
            return json.decodeFromString(data.questionsJson)
        }
    }
}

// In QuestionScreen when quiz finishes:
navController.navigate(
    QuizResultScreen.create(
        questions = questions,
        categoryName = category.name,
        categoryIcon = category.icon,
        initialScore = score
    )
)

// In Navigation graph:
composable<QuizResultScreen> { backStackEntry ->
    val quizResultScreen: QuizResultScreen = backStackEntry.toRoute()
    val quizResultViewModel: QuizResultViewModel = hiltViewModel()

    quizResultViewModel.setQuizData(
        questions = QuizResultScreen.getQuestions(quizResultScreen),
        category = com.neo.trivia.domain.model.Category(
            id = 0,
            name = quizResultScreen.categoryName,
            icon = quizResultScreen.categoryIcon
        )
    )

    QuizResultScreen(viewModel = quizResultViewModel)
}
```

Each screen receives its own ViewModel instance via Hilt injection.

### How JSON Serialization Works

1. **Creation:** `QuizResultScreen.create()` serializes the `List<Question>` to JSON string
2. **Navigation:** Navigation serializes the entire data class (which now only has primitive/string types)
3. **Extraction:** `backStackEntry.toRoute()` deserializes to `QuizResultScreen`
4. **Deserialization:** `QuizResultScreen.getQuestions()` converts JSON string back to `List<Question>`
5. **ViewModel:** ViewModel receives the list and initializes the screen

### Why JSON Serialization?

- Jetpack Compose Navigation only supports primitive types and `@Serializable` types that are already registered in the Navigation type registry
- `List<T>` is not automatically supported by Navigation's serialization
- JSON serialization provides a portable format that can be safely serialized and deserialized
- The approach is lightweight and doesn't require external dependencies

---

## Data Persistence

The app persists quiz results locally using Room database with the following features:

### QuizResultEntity Schema

```kotlin
@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val categoryName: String,
    val categoryIcon: String,
    val score: Int,
    val totalQuestions: Int,
    val questionsJson: String,
    val quizResultsJson: String
)
```

### Data Flow for Quiz Results

1. **On Quiz Completion**: `QuestionViewModel.onAnswerSelected()` calls `saveResultToDatabase()`
2. **Save to Database**: `SaveQuizResultUseCase.save()` saves the result with:
   - Category information (name and icon)
   - Score and total questions
   - All questions as JSON (for review)
   - All answer results as JSON (for detailed breakdown)
3. **Result Limit**: Automatically deletes oldest results if more than 10 exist
4. **History Loading**: `QuizResultViewModel.loadSavedResults()` loads all recent results from database
5. **Screen Display**: Results are displayed in `QuizResultScreen` with score, percentage, and detailed breakdown

### Key Implementation Details

- **Max Results**: Database maintains maximum of 10 recent results
- **JSON Serialization**: Uses Gson to serialize complex types (Question[], QuizResult[])
- **Timestamp Ordering**: Results are ordered by timestamp (newest first)
- **DAO Methods**:
  - `insert()` - Save a new result
  - `getRecentResults()` - Get all results ordered by timestamp (Flow-based)
  - `getLatestResult()` - Get the most recent result (Flow-based)
  - `deleteOldest(count)` - Delete oldest results when exceeding limit
  - `clearAll()` - Clear all results (for testing)

### LocalDataSource Integration

The `LocalDataSource` interface provides high-level methods for quiz result queries:

```kotlin
interface LocalDataSource {
    // ... existing methods ...

    /**
     * Retrieves all recent quiz results.
     * @return Flow of domain QuizResult objects
     */
    fun getQuizResults(): Flow<List<QuizResult>>

    /**
     * Retrieves the most recent quiz result.
     * @return Flow of the latest domain QuizResult object or null
     */
    fun getLatestQuizResult(): Flow<QuizResult?>
}
```

This integration allows ViewModels to query quiz results without directly accessing DAOs, maintaining clean architecture principles.