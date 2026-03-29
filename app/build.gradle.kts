import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.apkanalyzer.application)
    alias(libs.plugins.apkanalyzer.hilt)
    alias(libs.plugins.apkanalyzer.spotless)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.dependency.updates)
}

extensions.configure<ApplicationExtension> {
    namespace = "sk.styk.martin.apkanalyzer"

    defaultConfig {
        applicationId = "sk.styk.martin.apkanalyzer"
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        versionCode = 1
        versionName = "dev"
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
        buildConfig = true
    }

    androidResources {
        generateLocaleConfig = true
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

    implementation(project(":core:app-analysis-core"))
    implementation(project(":core:app-list"))
    implementation(project(":core:app-permissions"))
    implementation(project(":core:app-statistics"))
    implementation(project(":core:common"))
    implementation(project(":core:ui-library"))

    ksp(libs.glide.compiler)
    implementation(libs.bundles.google.play)

    debugImplementation(libs.leakcannary)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}
