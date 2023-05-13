package sk.styk.martin.apkanalyzer.ui

import androidx.appcompat.app.AppCompatActivity
import sk.styk.martin.apkanalyzer.manager.analytics.FragmentScreenTracker
import sk.styk.martin.apkanalyzer.manager.backpress.BackPressedManager
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.ForegroundFragmentWatcher
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.manager.resources.ActivityColorThemeManager
import javax.inject.Inject

abstract class ApkAnalyzerBaseActivity : AppCompatActivity() {

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var activityColorThemeManager: ActivityColorThemeManager

    @Inject
    lateinit var backPressedManager: BackPressedManager

    @Inject
    lateinit var fragmentScreenTracker: FragmentScreenTracker

    @Inject
    lateinit var foregroundFragmentWatcher: ForegroundFragmentWatcher

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPressedManager.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}
