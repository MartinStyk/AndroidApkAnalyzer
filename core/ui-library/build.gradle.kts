plugins {
    id("apkanalyzer.library")
    id("apkanalyzer.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.uilibrary"
}

dependencies {
    implementation(libs.material)
}
