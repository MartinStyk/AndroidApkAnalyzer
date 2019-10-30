package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ServiceData(
        val name: String,
        val permission: String? = null, //Optional name of a permission required to be able to access this Service
        val isExported: Boolean = false, //May be called by another activity
        var isStopWithTask: Boolean = false, //The service will automatically be stopped by the system if the user removes a task that is rooted in one of the application's activities
        var isSingleUser: Boolean = false, //Single instance of the service will run for all users on the device
        var isIsolatedProcess: Boolean = false, //The service will run in its own isolated process
        var isExternalService: Boolean = false //The service can be bound and run in the calling application's package, rather than the package in which it is declared
) : Parcelable

