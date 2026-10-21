package sk.styk.martin.apkanalyzer.feature.appdetail.phone.certificates

internal sealed interface CertificatesEvent {
    data object ShowCopiedFeedback : CertificatesEvent
}
