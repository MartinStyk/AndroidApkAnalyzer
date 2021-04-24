package sk.styk.martin.apkanalyzer.ui.permission.detail

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ActivityPermissionDetailBinding
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.ApkAnalyzerBaseActivity
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment.Companion.ARG_PERMISSIONS_DATA
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.util.TAG_PERMISSIONS
import timber.log.Timber

@AndroidEntryPoint
class PermissionDetailActivity : ApkAnalyzerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityPermissionDetailBinding>(this, R.layout.activity_permission_detail)

        if (savedInstanceState == null) {
            val permissionData = intent.getParcelableExtra<LocalPermissionData>(ARG_PERMISSIONS_DATA)
            if (permissionData == null) {
                Timber.tag(TAG_PERMISSIONS).e(IllegalStateException("No permission data"))
                return
            }
            val fragment = PermissionDetailFragment.create(
                    permissionData = permissionData
            )
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment, FragmentTag.PermissionDetail.toString())
                    .commit()
        }
    }

}
