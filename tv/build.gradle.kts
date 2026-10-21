plugins {
    alias(libs.plugins.apkanalyzer.application)
    alias(libs.plugins.apkanalyzer.hilt)
    alias(libs.plugins.apkanalyzer.compose)
    alias(libs.plugins.apkanalyzer.spotless)
}

android {
    namespace = "sk.styk.martin.apkanalyzer.tv"

    defaultConfig {
        applicationId = "sk.styk.martin.apkanalyzer"
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        versionCode = (project.findProperty("version.code") as String).toInt()
        versionName = project.findProperty("version.name") as String
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.core.designSystem.tv)

    implementation(projects.feature.apps.api)
    implementation(projects.feature.browse.api)

    implementation(libs.androidx.activity.compose)
}
