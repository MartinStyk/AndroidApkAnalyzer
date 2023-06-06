import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import sk.styk.martin.apkanalyzer.MinSdk
import sk.styk.martin.apkanalyzer.TargetSdk
import sk.styk.martin.apkanalyzer.configureKotlin

class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = TargetSdk
                defaultConfig {
                    minSdk = MinSdk
                }
                configureKotlin(this)
            }
        }
    }
}
