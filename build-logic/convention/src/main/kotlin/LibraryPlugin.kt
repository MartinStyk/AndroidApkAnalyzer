import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import sk.styk.martin.apkanalyzer.CompileSdk
import sk.styk.martin.apkanalyzer.MinSdk

class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = CompileSdk
                defaultConfig {
                    minSdk = MinSdk
                }
            }

            configureKotlin()
        }
    }
}
