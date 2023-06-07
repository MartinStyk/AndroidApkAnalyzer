plugins {
    id("apkanalyzer.library")
    id("apkanalyzer.hilt")
    id("kotlin-parcelize")
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
