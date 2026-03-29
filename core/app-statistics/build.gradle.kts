plugins {
    alias(libs.plugins.apkanalyzer.library)
    alias(libs.plugins.apkanalyzer.hilt)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.appstatistics"
}

dependencies {
    implementation(project(":core:app-analysis-core"))
    implementation(project(":core:app-list"))
    implementation(project(":core:common"))
}
