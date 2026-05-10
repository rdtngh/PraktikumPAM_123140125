# MockK rules
-keep class io.mockk.** { *; }
-keep interface io.mockk.** { *; }
-keep class io.mockk.proxy.android.** { *; }
-dontwarn io.mockk.**

# SQLDelight rules
-keep class com.example.tugaspam3.db.** { *; }

# Koin rules
-keep class org.koin.** { *; }
