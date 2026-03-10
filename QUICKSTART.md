# Quick Start Guide 🚀

## Prerequisites

1.  **Android Studio**: Version Ladybug (2024.2.1) or later.
2.  **Java JDK**: Version 17 or higher.
3.  **Gradle**: Version 8.0 or higher.

## Installation Steps

### 1. Open the Project
```bash
# Navigate to the project directory
cd C:\Users\nehem\AndroidStudioProjects\TriviaApp

# Open in Android Studio: File > Open > Select this directory
```

### 2. Sync Gradle
Use Android Studio's **Sync Project with Gradle Files** button or run:
```bash
./gradlew build --refresh-dependencies
```

### 3. Build & Run
1.  Connect an Android device or start an emulator.
2.  Click **Run** (Green play button) in Android Studio.
3.  The app will build, install, and launch.

---

## First Run Experience

1.  **Home Screen**: Browse through a vibrant grid of trivia categories.
2.  **Background Sync**: On the first run, the app will automatically start downloading 20+ questions for each category to enable **Offline Mode**.
3.  **Select Category**: Tap a category (e.g., Science, History, Music) to see it highlighted in the theme's accent color.
4.  **Pick Difficulty**: Choose between Easy, Medium, or Hard.
5.  **Start Quiz**: Tap "Start Quiz" to begin.
6.  **Review Results**: After 10 questions, you'll see a detailed breakdown of your performance, including which questions you got wrong and the correct answers.

---

## Key Features Guide

### 📶 Offline Mode
The app is designed to work without internet. Once the initial sync is complete:
- You can play quizzes even in Airplane Mode.
- Only categories with cached questions will be visible when offline.
- Quizzes are randomized locally using SQL for a fresh experience every time.

### 🎨 Theming
The app defaults to the **Playful** theme (Deep Purple & Amber). You can change this in Settings:
- **Playful**: Classic trivia feel.
- **Vibrant**: Energetic Orange & Blue.
- **Ocean**: Modern Teal & Blue.
- **Sunset**: Warm Purple & Orange.
- **Mint**: Fresh Green & Teal.

### ⭐ Favorites & History
- **Favorites**: Long-press or tap the favorite icon during a quiz to save a question.
- **History**: Check the "Stats" tab to see your past scores and revisit detailed result reviews for every quiz you've played.

---

## Troubleshooting

### Icon Not Updating
If you see the old Android logo on the home screen:
1.  Uninstall the app from your device.
2.  In Android Studio: **Build > Clean Project**.
3.  Run the app again.

### Offline Quiz has < 10 Questions
This usually means the initial sync hasn't finished or was interrupted.
- Stay online for a minute to let the `SyncQuestionsUseCase` finish.
- The app will automatically "fill" the quiz with random questions from other categories if your selected one is short on data.

---

## Development Reference

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

### Updating Icons
Launcher icons are located in `app/src/main/res/drawable/ic_launcher_foreground.xml` and `ic_launcher_background.xml`. The adaptive icon definition is in `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`.

Happy coding! 🎮
