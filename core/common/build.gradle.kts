plugins {
    id("apkanalyzer.library")
    id("apkanalyzer.hilt")
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.common"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.palette.ktx)
    implementation(project(":core:ui-library"))
}
