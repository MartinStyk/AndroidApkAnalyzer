plugins {
    alias(libs.plugins.apkanalyzer.feature.phone)
}

android {
    namespace = "sk.styk.martin.apkanalyzer.feature.settings.phone"
}

dependencies {
    api(projects.feature.settings.api)
    implementation(projects.core.common)
}
