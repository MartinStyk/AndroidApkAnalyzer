package sk.styk.martin.apkanalyzer.feature.appdetail.phone.appcomponents

internal sealed interface IntentFiltersEvent {
    data object NavigateBack : IntentFiltersEvent
    data object ShowCopiedFeedback : IntentFiltersEvent
}
