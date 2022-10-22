plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("kotlin-parcelize")
    kotlin("android")
    kotlin("kapt")
}
android {
    namespace = "sk.styk.martin.apkanalyzer"

    defaultConfig {
        applicationId = "sk.styk.martin.apkanalyzer"

        minSdk = 21
        targetSdk = 32
        compileSdk = 32

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
}

dependencies {
    val lifecycleVersion: String by rootProject.extra
    val coroutinesVersion: String by rootProject.extra
    val kotlinVersion: String by rootProject.extra
    val hiltVersion: String by rootProject.extra

    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.fragment:fragment-ktx:1.5.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0-alpha")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.10")
    implementation("com.pddstudio:highlightjs-android:1.5.0")
    implementation("com.github.AppIntro:AppIntro:6.1.0")
    implementation("net.cachapa.expandablelayout:expandablelayout:2.9.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation("com.google.android.play:core:1.10.3")
    implementation("com.google.android.play:core-ktx:1.8.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:27.0.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-perf")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")
    testImplementation("junit:junit:4.13.2")
}