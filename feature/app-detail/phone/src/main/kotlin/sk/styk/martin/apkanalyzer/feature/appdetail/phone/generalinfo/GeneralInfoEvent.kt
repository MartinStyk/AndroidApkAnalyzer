package sk.styk.martin.apkanalyzer.feature.appdetail.phone.generalinfo

sealed interface GeneralInfoEvent {
    data object ShowCopiedFeedback : GeneralInfoEvent
}
