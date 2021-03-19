package sk.styk.martin.apkanalyzer.manager.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ApplicationScope
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

private const val PERMISSIONS_REQUEST_CODE = 777

class PermissionsManagerImpl @Inject constructor(
        @ApplicationScope private val context: Context,
) : ViewModel(), PermissionManager {

    private var activityWeakReference: WeakReference<Activity>? = null
    private val activity: Activity?
        get() = activityWeakReference?.get()

    @Volatile
    private var permissionsCallback: PermissionManager.PermissionsCallback? = null

    override fun hasPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun shouldShowRationaleForPermission(permission: String): Boolean {
        val activity = activity
        return activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    override fun requestPermission(permission: String, callback: PermissionManager.PermissionCallback) {
        requestPermissions(arrayOf(permission), object : PermissionManager.PermissionsCallback {
            override fun onPermissionsDenied(deniedPermissions: List<String>) {
                callback.onPermissionDenied(deniedPermissions[0])
            }

            override fun onPermissionsGranted(grantedPermissions: List<String>) {
                callback.onPermissionGranted(grantedPermissions[0])
            }
        })
    }

    @Synchronized
    override fun requestPermissions(permissions: Array<String>, callback: PermissionManager.PermissionsCallback) {
        val activity = activity
        if (activity != null) {
            permissionsCallback = callback
            ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    @Synchronized
    override fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray) {
        val granted: MutableList<String> = ArrayList()
        val denied: MutableList<String> = ArrayList()
        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permissions[i])
            } else {
                denied.add(permissions[i])
            }
        }
        if (granted.isNotEmpty()) {
            permissionsCallback?.onPermissionsGranted(granted)
        }
        if (denied.isNotEmpty()) {
            permissionsCallback?.onPermissionsDenied(denied)
        }
        permissionsCallback = null
    }

    fun bind(activity: AppCompatActivity) {
        activityWeakReference = WeakReference(activity)
    }

}
