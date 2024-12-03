// build.gradle.kts (Project level)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
subprojects {
    plugins.apply("com.android.application")
    plugins.apply("org.jetbrains.kotlin.android")
}