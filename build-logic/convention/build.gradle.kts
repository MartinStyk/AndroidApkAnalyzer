plugins {
    `kotlin-dsl`
}

group = "sk.styk.martin.apkanalyzer.buildlogic"

kotlin {
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.spotless.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("apkanalyzer.hilt") {
            id = "apkanalyzer.hilt"
            implementationClass = "HiltPlugin"
        }
        register("apkanalyzer.library") {
            id = "apkanalyzer.library"
            implementationClass = "LibraryPlugin"
        }
        register("apkanalyzer.application") {
            id = "apkanalyzer.application"
            implementationClass = "ApplicationPlugin"
        }
        register("apkanalyzer.spotless") {
            id = "apkanalyzer.spotless"
            implementationClass = "SpotlessPlugin"
        }
    }
}
