pluginManagement {
    includeBuild("build-logic")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        @Suppress("JcenterRepositoryObsolete")
        jcenter()
    }
}

rootProject.name = "apk-analyzer"
include(":app")
include(":core:app-analysis-core")
include(":core:app-list")
include(":core:app-permissions")
include(":core:app-statistics")
include(":core:common")
include(":core:ui-library")