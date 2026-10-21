plugins {
    alias(libs.plugins.apkanalyzer.library)
    alias(libs.plugins.apkanalyzer.hilt)
    alias(libs.plugins.apkanalyzer.compose)
}

android {
    namespace = "sk.styk.martin.apkanalyzer.core.designsystem.foundation"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.core)
    implementation(projects.core.common)
}
