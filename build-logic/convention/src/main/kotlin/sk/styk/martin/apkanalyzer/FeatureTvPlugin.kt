package sk.styk.martin.apkanalyzer

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import sk.styk.martin.apkanalyzer.utils.implementation

class FeatureTvPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("apkanalyzer.library")
            apply("apkanalyzer.hilt")
            apply("apkanalyzer.compose")
        }

        dependencies {
            implementation(project(":core:design-system:tv"))
            implementation(project(":core:navigation"))
        }
    }
}
