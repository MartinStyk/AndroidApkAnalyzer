plugins {
    alias(libs.plugins.apkanalyzer.feature.tv)
}

android {
    namespace = "sk.styk.martin.apkanalyzer.feature.apps.tv"
}

dependencies {
    api(projects.feature.apps.api)
    implementation(projects.feature.appDetail.api)
    implementation(projects.core.apps)
    implementation(projects.core.common)
    implementation(projects.core.userPreferences)
}
