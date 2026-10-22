package sk.styk.martin.apkanalyzer.feature.appdetail.phone.requirements

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.core.apps.devicefeatures.RequirementSection

@Immutable
sealed interface RequirementsState {
    data object Loading : RequirementsState

    @Immutable
    data class Loaded(val sections: List<RequirementSection>, val missingRequiredCount: Int) : RequirementsState {
        val hasRequirements: Boolean
            get() = sections.isNotEmpty()
    }

    data object Error : RequirementsState
}
