package sk.styk.martin.apkanalyzer.ui.activity

import dagger.android.support.DaggerAppCompatActivity
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import javax.inject.Inject

abstract class ApkAnalyzerBaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var permissionManager: PermissionManager

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(permissions, grantResults)
    }

}