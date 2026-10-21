package sk.styk.martin.apkanalyzer.feature.appdetail.phone.requirements

sealed interface RequirementsEvent {
    data object ShowCopiedFeedback : RequirementsEvent
}
