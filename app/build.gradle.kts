plugins {
    // Android studio bug shows libs as an error - https://youtrack.jetbrains.com/issue/KTIJ-19369
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.spotless)
    alias(libs.plugins.dependency.updates)
    id("kotlin-parcelize")
    kotlin("kapt")
}
android {
    namespace = "sk.styk.martin.apkanalyzer"

    defaultConfig {
        applicationId = "sk.styk.martin.apkanalyzer"

        minSdk = 21
        targetSdk = 33
        compileSdk = 33

        versionCode = 1
        versionName = "dev"

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
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
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn " +
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi " +
                "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
    }
    lint {
        disable += "MissingTranslation"
    }
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint("0.48.2")
        }
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

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-perf")

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    debugImplementation(libs.leakcannary)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}