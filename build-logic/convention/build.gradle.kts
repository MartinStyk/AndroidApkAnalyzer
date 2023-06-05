import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "sk.styk.martin.apkanalyzer.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
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
