package sk.styk.martin.apkanalyzer.feature.apps.phone.filter.source

sealed interface SourceFilterEvent {
    data object NavigateBack : SourceFilterEvent
}
