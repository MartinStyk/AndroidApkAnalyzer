package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Represents data obtained from certificate file
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class CertificateData(
        var signAlgorithm: String,
        var certificateHash: String,
        var publicKeyMd5: String,
        var startDate: Date,
        var endDate: Date,
        var serialNumber: Int = 0,
        var issuerName: String? = null,
        var issuerOrganization: String? = null,
        var issuerCountry: String? = null,
        var subjectName: String? = null,
        var subjectOrganization: String? = null,
        var subjectCountry: String? = null
) : Parcelable

