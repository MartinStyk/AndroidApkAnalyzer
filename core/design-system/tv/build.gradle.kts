plugins {
    alias(libs.plugins.apkanalyzer.library)
    alias(libs.plugins.apkanalyzer.compose)
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.designsystem.tv"

    androidResources.enable = false
}

dependencies {
    api(projects.core.designSystem.foundation)
    implementation(libs.androidx.tv.material)
}
