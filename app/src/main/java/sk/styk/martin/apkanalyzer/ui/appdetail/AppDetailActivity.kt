package sk.styk.martin.apkanalyzer.ui.appdetail

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ActivityAppDetailBinding
import sk.styk.martin.apkanalyzer.ui.ApkAnalyzerBaseActivity

@AndroidEntryPoint
open class AppDetailActivity : ApkAnalyzerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityAppDetailBinding>(this, R.layout.activity_app_detail)

        if (savedInstanceState == null) {
            val detailRequest = getDetailRequestBundle()

            if (detailRequest == null) {
                Toast.makeText(this, R.string.error_loading_package_detail, Toast.LENGTH_LONG).show()
                finishAffinity()
            }

            val fragment = AppDetailFragment().apply {
                arguments = detailRequest
            }
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, AppDetailFragment.TAG)
                    .commit()
        }
    }

    protected open fun getDetailRequestBundle(): Bundle? = intent.extras

    companion object {
        const val APP_DETAIL_REQUEST = "appDetailRequest"
    }

}
