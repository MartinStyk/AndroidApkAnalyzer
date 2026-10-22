package sk.styk.martin.apkanalyzer.feature.appdetail.tv.requirements

internal sealed interface TvRequirementsAction {
    data object Retry : TvRequirementsAction
}
