plugins {
    id("apkanalyzer.library")
    id("apkanalyzer.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.applist"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:app-analysis-core"))

    implementation(libs.timber)
}
