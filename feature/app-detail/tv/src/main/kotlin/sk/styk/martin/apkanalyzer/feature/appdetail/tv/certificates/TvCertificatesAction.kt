package sk.styk.martin.apkanalyzer.feature.appdetail.tv.certificates

internal sealed interface TvCertificatesAction {
    data object Retry : TvCertificatesAction
}
