# Quick Start Guide

## Prerequisites

1. **Android Studio**: Version Koala (2023.1.1) or later
2. **Java JDK**: Version 11 or higher
3. **Gradle**: Version 8.0 or higher

## Installation Steps

### 1. Open the Project

```bash
# Navigate to the project directory
cd C:\Users\nehem\AndroidStudioProjects\TriviaApp

# Open in Android Studio
# File > Open > Select this directory
```

### 2. Sync Gradle

```bash
# Use Android Studio's Sync Gradle button
# Or run from terminal:
./gradlew build --refresh-dependencies
```

### 3. Build the App

```bash
# Build the project
./gradlew assembleDebug

# Or use Android Studio's Build > Rebuild Project
```

### 4. Run the App

1. Connect an Android device or start an emulator
2. Click "Run" in Android Studio
3. The app will install and launch

## First Run

When you open the app:

1. **Trivia Screen**: You'll see the main trivia screen
2. **Select Category**: Choose from 10 available categories (General Knowledge, Movies, History, etc.)
3. **Select Question Count**: Choose 10, 20, 30, 40, or 50 questions
4. **Start Quiz**: Tap the "Start Quiz" button
5. **Answer Questions**: Select your answers for each question
6. **View Results**: See your score at the end
7. **Explore More**: Navigate to Favorites and Statistics screens

## Features Overview

### 🎯 Main Trivia Screen
- Category filtering (10 categories)
- Question count selection (10, 20, 30, 40, 50)
- Real-time question display
- Answer checking
- Score tracking
- Navigation to results

### ⭐ Favorites Screen
- View saved favorite questions
- Search through favorites
- Toggle favorite status
- Beautiful card-based UI

### 📊 Statistics Screen
- Total questions count
- Favorite questions count
- Usage information

## Troubleshooting

### Java Not Found

```bash
# Check Java installation
java -version

# If not found, set JAVA_HOME environment variable
# Windows:
set JAVA_HOME=C:\Program Files\Java\jdk-11
set PATH=%JAVA_HOME%\bin;%PATH%

# Or use Android Studio's bundled JDK
```

### Gradle Sync Issues

```bash
# Clean Gradle cache
./gradlew clean

# Delete .gradle folder
rm -rf .gradle
# Or in Windows:
# Remove .gradle folder manually
```

### Build Errors

```bash
# Update dependencies
./gradlew build --refresh-dependencies

# Check build configuration
./gradlew tasks
```

## Development Tips

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

### Creating New Use Cases

1. Create file in `domain/usecase/`
2. Implement use case logic
3. Add DI provider in `domain/di/DomainModule.kt`
4. Use in ViewModel

### Adding New Categories

1. Add to `Category` enum in both:
   - `data/api/Category.kt`
   - `domain/model/Category.kt`
2. The API will automatically handle category filtering

### Modifying Database

```kotlin
// Add new field to entity
@Entity(tableName = "new_table")
data class NewEntity(
    @PrimaryKey val id: String,
    val newField: String
)

// Add new field to DAO
@Query("SELECT * FROM new_table")
fun getAllNewItems(): Flow<List<NewEntity>>
```

## Project Structure Reference

```
TriviaApp/
├── app/
│   └── src/main/java/com/neo/trivia/
│       ├── data/          # Data layer
│       │   ├── api/       # API interfaces and models
│       │   ├── database/  # Room database entities & DAOs
│       │   ├── di/        # Data DI module
│       │   ├── model/     # Data models
│       │   └── repository/# Repository implementation
│       ├── domain/        # Domain layer
│       │   ├── di/        # Domain DI module
│       │   ├── model/     # Domain models
│       │   ├── repository/# Repository interface
│       │   └── usecase/   # Business logic use cases
│       ├── ui/            # Presentation layer
│       │   ├── components/# Reusable UI components
│       │   ├── favorites/ # Favorites screen
│       │   ├── navigation/# Navigation setup
│       │   ├── stats/     # Statistics screen
│       │   ├── theme/     # Theme configuration
│       │   └── trivia/    # Trivia screen
│       └── MainActivity.kt
├── gradle/
│   └── libs.versions.toml  # Dependency versions
└── README.md              # Project documentation
```

## Next Steps for Customization

1. **Add Firebase**: Implement multiplayer features
2. **Add Sounds**: Integrate sound effects for correct/incorrect answers
3. **Add Animations**: Enhance UI with Compose animations
4. **Add Dark Mode**: Customize theme colors
5. **Add Leaderboards**: Implement Firebase Realtime Database for leaderboards
6. **Add Categories**: Add more trivia categories from the API
7. **Add Testing**: Write unit and integration tests

## Support

For issues or questions:
- Check `IMPLEMENTATION_SUMMARY.md` for detailed information
- Review the code comments in each file
- Check Android Studio's built-in documentation

## API Reference

Open Trivia DB API:
- URL: https://opentdb.com/api.php
- Documentation: https://opentdb.com/api_config.php

## License

This project is for educational purposes.

Happy coding! 🎮