# Deployment Guide: EcoViaje

This guide details how to build your signed Android App Bundle (AAB) and upload it to the Google Play Store.

## 1. Enable Release Build
I previously reverted the release configuration to keep your project stable in the development environment. You need to re-enable it on your machine.

1.  Open `app/build.gradle.kts`.
2.  Locate the `android { ... }` block.
3.  Replace the `signingConfigs` and `buildTypes` sections with the following:

```kotlin
    // Load keystore.properties
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = java.util.Properties()
    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true // Enables code shrinking (R8)
            isShrinkResources = true // Removes unused resources
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
```

## 2. Build the App Bundle (AAB)
Google Play prefers App Bundles (.aab) over APKs because they allow Google to optimize the download size for each user's device.

1.  Open your terminal in the project root.
2.  Run the following command:
    ```powershell
    ./gradlew bundleRelease
    ```
3.  **Success:** You should see `BUILD SUCCESSFUL`.
4.  **Locate File:** The file will be generated at:
    `app/build/outputs/bundle/release/app-release.aab`

## 2.1 Alternative: Build APK for Local Testing
If you don't have a Play Store account yet, you can build an **APK** to install directly on your phone.

1.  Run:
    ```powershell
    ./gradlew assembleRelease
    ```
2.  **Locate File:**
    `app/build/outputs/apk/release/app-release.apk`
3.  **Install:** Transfer this file to your phone and tap to install (you may need to enable "Install from Unknown Sources").

## 3. Upload to Google Play Console

1.  **Account:** Go to [Google Play Console](https://play.google.com/console) and sign in (requires a developer account, $25 one-time fee).
2.  **Create App:** Click **Create app**.
    *   **App Name:** EcoViaje
    *   **Default Language:** Spanish (es-419 or es-ES)
    *   **App or Game:** App
    *   **Free or Paid:** Free
3.  **Dashboard:** You will be taken to the dashboard. Follow the "Set up your app" checklist (Privacy Policy, App Access, Content Ratings, etc.).
4.  **Create Release:**
    *   Go to **Testing > Internal testing** (recommended for first upload) or **Production**.
    *   Click **Create new release**.
    *   **App Bundles:** Drag and drop your `app-release.aab` file here.
    *   **Release Name:** e.g., "1.0 - Initial Release".
    *   **Release Notes:** "Lanzamiento inicial de EcoViaje con reservas y pagos."
5.  **Review & Rollout:** Click **Next**, review any warnings, and then **Save** and **Start rollout**.

## Troubleshooting
*   **R8/Proguard Errors:** If the build fails with "Missing class" or similar errors, try setting `isMinifyEnabled = false` in `build.gradle.kts` temporarily.
*   **Keystore Errors:** Ensure `keystore.properties` has the correct passwords (currently set to "password" for this demo).
