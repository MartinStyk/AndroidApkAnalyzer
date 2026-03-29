import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import sk.styk.martin.apkanalyzer.CompileSdk
import sk.styk.martin.apkanalyzer.MinSdk
import sk.styk.martin.apkanalyzer.TargetSdk

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("com.google.gms.google-services")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = CompileSdk
                defaultConfig {
                    targetSdk = TargetSdk
                    minSdk = MinSdk
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
            }

            configureKotlin()

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                add("implementation", platform(bom))
                "implementation"(libs.findLibrary("firebase.analytics").get())
                "implementation"(libs.findLibrary("firebase.performance").get())
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
            }
        }
    }
}