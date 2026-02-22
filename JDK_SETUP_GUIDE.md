# JDK 17 Setup Guide for Windows

This guide will help you set up JDK 17 on your Windows PC for the Trivia App.

## Method 1: Install from Oracle (Recommended)

### Step 1: Download JDK 17

1. Go to [Oracle JDK Downloads](https://www.oracle.com/java/technologies/downloads/#java17)
2. Under "Java SE Development Kit 17", click **"Windows x64 Installer"**
3. Download the file (approximately 200 MB)

### Step 2: Install JDK 17

1. Run the downloaded installer
2. Click "Next" through the installation prompts
3. **Important**: Make sure to note the installation path (default is usually: `C:\Program Files\Java\jdk-17`)
4. Click "Finish" when done

### Step 3: Verify Installation

1. Open Command Prompt (Win+R, type `cmd`, press Enter)
2. Type: `java -version`
3. Type: `javac -version`

You should see output like:
```
openjdk version "17.0.x" 2024-xx-xx
OpenJDK Runtime Environment (build 17.0.x+x)
OpenJDK 64-Bit Server VM (build 17.0.x+x, mixed mode)
```

## Method 2: Use Eclipse Temurin (Free & Recommended)

### Step 1: Download Eclipse Temurin

1. Go to [Eclipse Temurin Downloads](https://adoptium.net/)
2. Under "Temurin 17", click **"Windows x64 .msi Installer"** (or `.exe`)
3. Download the file

### Step 2: Install Eclipse Temurin

1. Run the downloaded installer
2. Click "Next" through the prompts
3. Choose the installation path (default is good)
4. **Important**: When asked "Set JAVA_HOME variable?", choose **"Set JAVA_HOME variable for all users"**
5. Click "Finish" when done

### Step 3: Verify Installation

1. Open Command Prompt (Win+R, type `cmd`, press Enter)
2. Type: `java -version`
3. Type: `javac -version`

## Method 3: Use Android Studio Bundled JDK (Easiest)

Android Studio comes with its own JDK. Let's configure your project to use it:

### Step 1: Find Android Studio JDK

1. Open Android Studio
2. Go to **File → Project Structure** (Ctrl+Alt+Shift+S)
3. Click **SDK Location** on the left
4. Look for **JDK location** - it shows the path
5. Note this path (usually: `C:\Users\<YourUsername>\AppData\Local\JetBrains\Toolbox\apps\AndroidStudio<Version>\jbr`)

### Step 2: Configure Project to Use Android Studio JDK

1. Open your project in Android Studio
2. Go to **File → Project Structure** (Ctrl+Alt+Shift+S)
3. Click **Project** on the left
4. Under **SDK**, select **Java 17** (if available)
5. Under **Language level**, select **17 - Sealed classes, patterns, etc.**
6. Click **Apply** and **OK**

### Step 3: Configure Gradle to Use JDK 17

Edit `gradle.properties` and add:

```properties
org.gradle.java.home=C:\\Users\\<YourUsername>\\AppData\\Local\\JetBrains\\Toolbox\\apps\\AndroidStudio<Version>\\jbr
```

Replace `<YourUsername>` and `<Version>` with your actual path.

## Configure Environment Variables

### Option 1: Using GUI

1. Right-click **"This PC"** or **"My Computer"** → **Properties**
2. Click **Advanced system settings**
3. Click **Environment Variables**
4. Under **System variables**, find and select **Path**
5. Click **Edit**
6. Click **New** and add:
   - For JDK 17: `C:\Program Files\Java\jdk-17\bin`
   - Or if using Android Studio: The JDK path from Step 1 above
7. Click **OK** on all windows

### Option 2: Using Command Prompt (Administrator)

```cmd
# Set JAVA_HOME
setx JAVA_HOME "C:\Program Files\Java\jdk-17"

# Set PATH
setx PATH "%PATH%;C:\Program Files\Java\jdk-17\bin"
```

## Verify and Update Gradle

### Update `gradle.properties`

Create or edit `gradle.properties` in the project root:

```properties
# Use JDK 17
org.gradle.java.home=C:\\Program Files\\Java\\jdk-17

# Additional Gradle optimizations
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true
```

### Update `gradle/wrapper/gradle-wrapper.properties`

Make sure it uses Gradle 8.x:

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-all.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

## Fix Hilt and KSP Issues

After setting up JDK 17, sync your project:

### In Android Studio:

1. **File → Invalidate Caches / Restart** (Ctrl+Shift+Alt+N, select "Invalidate Caches / Restart")
2. When Android Studio restarts:
   - **File → Sync Project with Gradle Files**
   - Wait for sync to complete

### In Command Prompt:

```cmd
cd C:\Users\nehem\AndroidStudioProjects\TriviaApp
gradlew clean
gradlew build
```

## Troubleshooting

### Issue: "java: invalid source release 17"

**Solution:** Make sure your JDK 17 is correctly installed and added to PATH.

```cmd
java -version
javac -version
```

### Issue: Gradle sync fails

**Solution:**
1. Delete `.gradle` folder: `C:\Users\nehem\.gradle`
2. Clean project: **Build → Clean Project**
3. Sync again: **File → Sync Project with Gradle Files**

### Issue: Hilt annotation processing not working

**Solution:**
1. Go to **File → Project Structure**
2. Click **Modules → trivia**
3. Go to **Sources** tab
4. Set **Language level** to **17 - Sealed classes, patterns, etc.**
5. Click **Apply** and **OK**

### Issue: KSP plugin errors

**Solution:**
Make sure your `gradle/libs.versions.toml` has the correct KSP version matching Kotlin:

```toml
kotlin = "2.3.10"
ksp = "2.3.10-1.0.29"
```

## Quick Test

Run these commands in Command Prompt to verify everything works:

```cmd
# Check Java version
java -version

# Check Java compiler
javac -version

# Check Gradle
cd C:\Users\nehem\AndroidStudioProjects\TriviaApp
gradlew build --stacktrace
```

## Preferred JDK 17 Install Path

If you installed via Oracle:
```
C:\Program Files\Java\jdk-17
```

If you installed via Eclipse Temurin:
```
C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot
```

If using Android Studio:
```
C:\Users\<YourUsername>\AppData\Local\JetBrains\Toolbox\apps\AndroidStudio<Version>\jbr
```

## Additional Resources

- [Oracle Java Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Eclipse Temurin](https://adoptium.net/)
- [Android Studio Setup](https://developer.android.com/studio/intro)
- [Gradle JDK Configuration](https://docs.gradle.org/current/userguide/build_environment.html)

## Summary

✅ Install JDK 17
✅ Set JAVA_HOME environment variable
✅ Add JDK to PATH
✅ Configure project to use JDK 17
✅ Sync and build the project

Your Trivia App should now compile successfully with Hilt and KSP! 🎉