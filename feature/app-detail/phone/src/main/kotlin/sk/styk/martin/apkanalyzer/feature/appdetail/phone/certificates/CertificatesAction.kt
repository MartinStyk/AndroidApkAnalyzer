package sk.styk.martin.apkanalyzer.feature.appdetail.phone.certificates

internal sealed interface CertificatesAction {
    data object Retry : CertificatesAction
    data class CopyValue(val label: String, val value: String) : CertificatesAction
}
