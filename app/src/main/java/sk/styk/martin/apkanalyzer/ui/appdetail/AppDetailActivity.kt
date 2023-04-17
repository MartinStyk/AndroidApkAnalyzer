package sk.styk.martin.apkanalyzer.ui.appdetail

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ActivityAppDetailBinding
import sk.styk.martin.apkanalyzer.ui.ApkAnalyzerBaseActivity
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragment.Companion.APP_DETAIL_REQUEST
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.util.TAG_APP_DETAIL
import timber.log.Timber

@AndroidEntryPoint
open class AppDetailActivity : ApkAnalyzerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityAppDetailBinding>(this, R.layout.activity_app_detail)

        if (savedInstanceState == null) {
            val detailRequest = getDetailRequestBundle()

            if (detailRequest?.getParcelable<AppDetailRequest>(APP_DETAIL_REQUEST) == null) {
                Toast.makeText(this, R.string.error_loading_package_detail, Toast.LENGTH_LONG).show()
                Timber.tag(TAG_APP_DETAIL).e(IllegalStateException("No app detail request"))
                finishAffinity()
                return
            }

            val fragment = AppDetailFragment().apply {
                arguments = detailRequest
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment, FragmentTag.AppDetailParent.tag)
                .commit()
        }
    }

    protected open fun getDetailRequestBundle(): Bundle? = intent.extras
}
