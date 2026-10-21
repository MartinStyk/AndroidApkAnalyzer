package sk.styk.martin.apkanalyzer.feature.appdetail.phone.components.aisummary

internal sealed interface AiSummaryAction {
    data object DownloadModel : AiSummaryAction
}
