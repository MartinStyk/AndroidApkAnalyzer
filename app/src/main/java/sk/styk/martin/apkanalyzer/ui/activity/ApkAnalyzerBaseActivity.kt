package sk.styk.martin.apkanalyzer.ui.activity

import dagger.android.support.DaggerAppCompatActivity
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import javax.inject.Inject

abstract class ApkAnalyzerBaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var backPressedManager: BackPressedManager

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(permissions, grantResults)
    }

    override fun onBackPressed() {
        if (backPressedManager.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

}