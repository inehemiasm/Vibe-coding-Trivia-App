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
- Implement DataStore for preference management (not SharedPreferences)
- Use Room for local database operations

### 3. Code Quality
- Follow KtLint formatting rules
- Maintain consistent naming conventions
- Use meaningful variable and function names
- Keep functions small and focused
- Write unit tests for business logic
- Write integration tests for data layer

## Specific Implementation Rules

### Data Layer
- Use DataStore for all preference management instead of SharedPreferences
- Implement Repository pattern for data access
- Use sealed classes for result handling (Success, Error, Loading)
- All database operations should be suspend functions
- Use proper error handling with try/catch blocks

### Domain Layer
- Keep business logic separate from UI logic
- Use use cases (interactors) to encapsulate business logic
- Return data classes from use cases
- Use sealed classes for error handling

### Presentation Layer
- Use ViewModel for UI state management
- Implement proper lifecycle handling
- Use Compose for declarative UI
- Handle UI state properly (loading, success, error)
- Use Hilt for dependency injection in ViewModels

### Testing
- Write unit tests for use cases and business logic
- Write instrumented tests for data layer
- Use MockK for mocking dependencies
- Test error scenarios
- Ensure 100% test coverage for critical business logic

## Architecture Skills

### 1. DataStore Implementation Skill
- Implement DataStore-based preference managers
- Handle migration from legacy SharedPreferences
- Use proper type safety with DataStore
- Implement proper error handling for data operations

### 2. Repository Pattern Skill
- Implement repository interfaces
- Create concrete repository implementations
- Handle data source switching (local/remote)
- Implement proper error propagation

### 3. ViewModel Architecture Skill
- Implement proper ViewModel with Hilt injection
- Handle UI state management correctly
- Use LiveData or StateFlow for state observation
- Implement proper lifecycle handling

### 4. Testing Skill
- Write comprehensive unit tests
- Implement integration tests
- Use proper mocking techniques
- Test error scenarios and edge cases
- Ensure test coverage metrics

### 5. Dependency Injection Skill
- Use Hilt for proper dependency injection
- Implement proper module structure
- Handle scoped dependencies correctly
- Use proper qualifiers for multiple implementations

## Code Review Guidelines

### 1. Architecture Compliance
- Verify clean architecture separation
- Check for proper dependency injection usage
- Ensure repository pattern is followed
- Validate use case implementation

### 2. Modern Practices
- Check for DataStore usage vs SharedPreferences
- Verify Coroutines usage patterns
- Ensure proper error handling
- Validate UI state management

### 3. Code Quality
- Check for KtLint compliance
- Verify naming conventions
- Ensure code readability
- Validate proper documentation

## Example Implementation Patterns

### DataStore Manager Pattern
```
class ThemePreferencesManager(private val context: Context) {
    // Implementation using DataStore
}
```

### Repository Pattern
```
interface QuizResultRepository {
    suspend fun saveResult(result: QuizResultEntity)
    suspend fun getRecentResults(): Flow<List<QuizResultEntity>>
}
```

### Use Case Pattern
```
class GetQuizResultsUseCase(private val repository: QuizResultRepository) {
    suspend operator fun invoke(): Result<List<QuizResult>> {
        // Implementation
    }
}
```

### ViewModel Pattern
```
@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val getQuizResultsUseCase: GetQuizResultsUseCase
) : ViewModel() {
    // Implementation
}
```