package sk.styk.martin.apkanalyzer.feature.appdetail.phone.splitapks

internal sealed interface SplitApksEvent {
    data object NavigateBack : SplitApksEvent

    data object ShowCopiedFeedback : SplitApksEvent
}
