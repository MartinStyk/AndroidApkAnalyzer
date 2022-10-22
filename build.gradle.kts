// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra["gradleVersion"] = "7.3.0"
    extra["kotlinVersion"] = "1.7.20"
    extra["hiltVersion"] = "2.44"
    extra["lifecycleVersion"] = "2.5.1"
    extra["coroutinesVersion"] = "1.6.4"

    val gradleVersion: String by extra
    val kotlinVersion: String by extra
    val hiltVersion: String by extra

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${gradleVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("com.google.firebase:perf-plugin:1.4.2")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}