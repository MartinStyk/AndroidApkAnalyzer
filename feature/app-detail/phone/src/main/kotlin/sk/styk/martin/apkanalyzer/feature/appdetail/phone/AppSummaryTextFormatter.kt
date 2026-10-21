package sk.styk.martin.apkanalyzer.feature.appdetail.phone

internal interface AppSummaryTextFormatter {
    fun summary(state: AppDetailState.Loaded): String
    fun clipLabel(appName: String): String
}
