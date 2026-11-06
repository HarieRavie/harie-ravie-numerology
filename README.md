# Harie Ravie's Numerology — Android Studio project

This project now contains **both Android experiences**:

1. **Native-style Android screen** — launches by default, built with native Android UI components.
2. **WebView version** — opens the responsive HTML/CSS/JavaScript interface using the same calculation and Tamil data.

## Features
- Original A–Z numerology letter mapping from the uploaded `script.js`
- Numeric input support
- Total numerology value and Tamil interpretation lookup
- Pyramid pattern generation with adjacent-pair reduction
- Two-digit interpretation lookup for numeric input (pairs from left to right)
- Share result through Android's share sheet
- Offline operation; interpretation data is bundled in app assets

## Project details
- App name: Harie Ravie's Numerology
- Application ID: `com.harieravie.numerology`
- Minimum Android version: Android 6.0 (API 23)
- Target SDK: 35
- Android Gradle Plugin: 8.7.3
- Java source compatibility: 17

## Build an APK
1. Install Android Studio with Android SDK Platform 35 and JDK 17.
2. Extract this ZIP.
3. Open the `NumerologyAndroid` folder in Android Studio.
4. Allow Gradle Sync to complete (internet is needed the first time to download Gradle dependencies).
5. Select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
6. Android Studio will create a debug APK at:
   `app/build/outputs/apk/debug/app-debug.apk`

For a signed release APK, use **Build → Generate Signed Bundle / APK**.

## Data and behavior
- `app/src/main/assets/numerology.json` is the active interpretation database.
- `numerology-26-01-25.json` is retained as an additional data file from the supplied ZIP.
- The native and WebView versions use the same original letter mapping and primary JSON database.
- The original source files are retained in the `original/` folder.

Numerology interpretations are traditional belief-based content and should not be treated as scientific or guaranteed predictions.
