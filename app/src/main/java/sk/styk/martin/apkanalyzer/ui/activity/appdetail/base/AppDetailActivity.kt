package sk.styk.martin.apkanalyzer.ui.activity.appdetail.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dagger.android.AndroidInjection
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ActivityAppDetailBinding
import sk.styk.martin.apkanalyzer.ui.activity.ApkAnalyzerBaseActivity

class AppDetailActivity : ApkAnalyzerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityAppDetailBinding>(this, R.layout.activity_app_detail)

        if (savedInstanceState == null) {
            val fragment = AppDetailFragment().apply {
                arguments = requireNotNull(this@AppDetailActivity.intent.extras)
            }
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, AppDetailFragment.TAG)
                    .commit()
        }
    }

    companion object {
        const val APP_DETAIL_REQUEST = "appDetailRequest"
    }

}
