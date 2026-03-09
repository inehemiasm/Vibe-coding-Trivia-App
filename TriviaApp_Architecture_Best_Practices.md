# TriviaApp Architecture Best Practices

## Core Principles

### 1. Clean Architecture
- Follow the layered architecture pattern (Presentation, Domain, Data)
- Maintain separation of concerns between layers
- Use dependency injection (Hilt) for loose coupling
- Keep business logic in domain layer, not in UI or data layers

### 2. Modern Android Development
- Use Kotlin as primary language
- Leverage Coroutines for asynchronous operations
- Use Jetpack Compose for UI development
- Implement DataStore for preference management
- Use Room for local database operations
- **Architecture Pattern**: Use MVI (Model-View-Intent) for the presentation layer.

### 3. Code Quality
- Follow KtLint formatting rules
- Maintain consistent naming conventions
- Use meaningful variable and function names
- Keep functions small and focused
- Write unit tests for business logic
- Write integration tests for data layer

## Specific Implementation Rules

### Data Layer
- Use DataStore for all preference management.
- Implement Repository pattern for data access.
- Use sealed classes or `Result` for result handling.
- All database operations should be suspend functions.
- Use proper error handling with try/catch blocks.

### Domain Layer
- Keep business logic separate from UI logic.
- Use use cases (interactors) to encapsulate business logic.
- Return data classes or `Result` from use cases.

### Presentation Layer (MVI)
- Use `BaseViewModel` to implement the MVI pattern.
- **UiState**: A single immutable data class representing the UI.
- **UiIntent**: User actions handled by the ViewModel.
- **UiEffect**: One-time side effects (navigation, snackbars).
- Use `collectAsStateWithLifecycle()` in Compose to observe state.
- Handle UI state properly (loading, success, error) within the state object.

### Testing
- Write unit tests for use cases and business logic.
- Write instrumented tests for the data layer.
- Use MockK for mocking dependencies.
- Test state transitions in ViewModels by asserting on `uiState`.

## Architecture Skills

### 1. DataStore Implementation Skill
- Implement DataStore-based preference managers.
- Use proper type safety with DataStore.

### 2. Repository Pattern Skill
- Implement repository interfaces and concrete implementations.
- Handle data source switching (local/remote).

### 3. MVI ViewModel Architecture Skill
- Extend `BaseViewModel` with specific State, Intent, and Effect types.
- Process intents in a centralized `handleIntent` function.
- Update state using `setState` and trigger effects using `sendEffect`.
- Maintain a single source of truth for UI state.

### 4. Dependency Injection Skill
- Use Hilt for proper dependency injection.
- Implement proper module structure.

## Example Implementation Patterns

### Use Case Pattern
```kotlin
class GetQuizResultsUseCase(private val repository: QuizResultRepository) {
    suspend operator fun invoke(): Result<List<QuizResult>> {
        // Implementation
    }
}
```

### MVI ViewModel Pattern
```kotlin
@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val getQuizResultsUseCase: GetQuizResultsUseCase
) : BaseViewModel<QuizResultUiState, QuizResultIntent, QuizResultUiEffect>(QuizResultUiState()) {

    override suspend fun handleIntent(intent: QuizResultIntent) {
        when (intent) {
            is QuizResultIntent.LoadResults -> loadResults()
        }
    }

    private fun loadResults() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            // ... fetch data and update state
            setState { copy(isLoading = false, results = fetchedResults) }
        }
    }
}
```
