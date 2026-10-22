package sk.styk.martin.apkanalyzer.feature.appdetail.tv.splitapks

internal sealed interface TvSplitApksAction {
    data object Retry : TvSplitApksAction
}
