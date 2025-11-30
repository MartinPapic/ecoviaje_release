# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\mapap\AppData\Local\Android\Sdk\tools\proguard\proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# Retrofit
-keep class com.appecoviaje.network.** { *; }
-keep interface com.appecoviaje.network.** { *; }

# Gson
-keep class com.appecoviaje.data.** { *; }

# Room
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }
-dontwarn androidx.room.paging.**

# ViewModels
-keep class com.appecoviaje.viewmodel.** { *; }

# Kotlin & Coroutines
-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**