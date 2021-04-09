package sk.styk.martin.apkanalyzer.ui.permission.detail

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dagger.android.AndroidInjection
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ActivityPermissionDetailBinding
import sk.styk.martin.apkanalyzer.ui.ApkAnalyzerBaseActivity
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment.Companion.ARG_PERMISSIONS_DATA

class PermissionDetailActivity : ApkAnalyzerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityPermissionDetailBinding>(this, R.layout.activity_permission_detail)

        if (savedInstanceState == null) {
            val fragment = PermissionDetailFragment.create(
                    permissionData = requireNotNull(intent.getParcelableExtra(ARG_PERMISSIONS_DATA))
            )
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment, PermissionDetailFragment.TAG)
                    .commit()
        }
    }

}
