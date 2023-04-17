package sk.styk.martin.apkanalyzer.ui.appdetail

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class AppDetailRequest : Parcelable {
    @Parcelize
    data class InstalledPackage(val packageName: String) : AppDetailRequest()

    @Parcelize
    data class ExternalPackage(val packageUri: Uri) : AppDetailRequest()
}
