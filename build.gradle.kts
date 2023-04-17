// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android studio bug shows libs as an error - https://youtrack.jetbrains.com/issue/KTIJ-19369
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.dependency.updates) apply false
}