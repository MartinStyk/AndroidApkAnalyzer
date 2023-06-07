plugins {
    id("apkanalyzer.library")
    id("apkanalyzer.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.appstatistics"
}

dependencies {
    implementation(project(":core:app-analysis-core"))
    implementation(project(":core:app-list"))

    implementation(libs.timber)
}
