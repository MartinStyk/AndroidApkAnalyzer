plugins {
    alias(libs.plugins.apkanalyzer.library)
    alias(libs.plugins.apkanalyzer.hilt)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.apppermissions"
}

dependencies {
    implementation(project(":core:app-analysis-core"))
    implementation(project(":core:app-list"))

    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
}
