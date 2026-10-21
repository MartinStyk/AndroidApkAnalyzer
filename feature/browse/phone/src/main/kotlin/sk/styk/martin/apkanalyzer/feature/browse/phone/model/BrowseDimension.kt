package sk.styk.martin.apkanalyzer.feature.browse.phone.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class BrowseDimension {
    Permission,
    SigningCertificate,
    TargetSdk,
    MinSdk,
    InstallSource,
    SharedUserId,
    AppCategory,
}
