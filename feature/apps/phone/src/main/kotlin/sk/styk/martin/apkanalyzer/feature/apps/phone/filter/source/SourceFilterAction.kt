package sk.styk.martin.apkanalyzer.feature.apps.phone.filter.source

import sk.styk.martin.apkanalyzer.core.common.model.AppSource

sealed interface SourceFilterAction {
    data class SourceToggled(val source: AppSource) : SourceFilterAction
    data object Reset : SourceFilterAction
    data object NavigateBack : SourceFilterAction
}
