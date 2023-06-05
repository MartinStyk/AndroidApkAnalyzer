plugins {
    // Android studio bug shows libs as an error - https://youtrack.jetbrains.com/issue/KTIJ-19369
    id("apkanalyzer.application")
    id("apkanalyzer.hilt")
    id("apkanalyzer.spotless")
    id("kotlin-parcelize")
    alias(libs.plugins.dependency.updates)
}
android {
    namespace = "sk.styk.martin.apkanalyzer"

    defaultConfig {
        applicationId = "sk.styk.martin.apkanalyzer"
        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }

    flavorDimensions += "version"
    productFlavors {
        create("free") {
            dimension = "version"
            buildConfigField("boolean", "SHOW_PROMO", "false")
        }
        create("premium") {
            dimension = "version"
            applicationIdSuffix = ".premium"
            versionNameSuffix = "-premium"
            buildConfigField("boolean", "SHOW_PROMO", "false")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    lint {
        disable += "MissingTranslation"
    }
}

dependencies {
    implementation(libs.koltin.stdlib.jdk8)
    implementation(libs.koltin.reflect)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.fragment.ktx)
    implementation(libs.preference.ktx)
    implementation(libs.bundles.lifecycle)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.legacy)

    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.palette.ktx)
    implementation(libs.mp.chart)
    implementation(libs.gif.drawable)
    implementation(libs.highlightjs)
    implementation(libs.appintro)
    implementation(libs.expandablelayout)
    implementation(libs.timber)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.bundles.google.play)

    debugImplementation(libs.leakcannary)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}