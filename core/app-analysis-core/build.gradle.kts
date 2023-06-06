plugins {
    id("apkanalyzer.library")
    id("apkanalyzer.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.appanalysis"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
}
