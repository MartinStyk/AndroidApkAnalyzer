package sk.styk.martin.apkanalyzer.ui.activity.mainactivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ActivityMainBinding
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactory
import sk.styk.martin.apkanalyzer.ui.about.AboutFragment
import sk.styk.martin.apkanalyzer.ui.activity.ApkAnalyzerBaseActivity
import sk.styk.martin.apkanalyzer.ui.activity.dialog.FeatureDialog
import sk.styk.martin.apkanalyzer.ui.activity.dialog.PromoDialog
import sk.styk.martin.apkanalyzer.ui.activity.intro.IntroActivity
import sk.styk.martin.apkanalyzer.ui.activity.localstatistics.LocalStatisticsFragment
import sk.styk.martin.apkanalyzer.ui.activity.premium.PremiumFragment
import sk.styk.martin.apkanalyzer.ui.activity.settings.SettingsFragment
import sk.styk.martin.apkanalyzer.ui.applist.main.MainAppListFragment
import sk.styk.martin.apkanalyzer.ui.permission.list.PermissionListFragment
import sk.styk.martin.apkanalyzer.util.*
import javax.inject.Inject

class MainActivity : ApkAnalyzerBaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModel = provideViewModel(viewModelFactory)

        with(viewModel) {
            closeDrawer.observe(this@MainActivity, { binding.drawerLayout.closeDrawer(GravityCompat.START) })
            openDrawer.observe(this@MainActivity, { binding.drawerLayout.openDrawer(GravityCompat.START) })
            premiumMenuItemVisible.observe(this@MainActivity, { binding.navigationView.menu.findItem(R.id.nav_premium).isVisible = it })
            placeInitialFragment.observe(this@MainActivity, { placeAppListFragment() })
            openAppList.observe(this@MainActivity, { popToAppList() })
            openStatistics.observe(this@MainActivity, { navigateTo(LocalStatisticsFragment(), FramentTag.LocalStatistics) })
            openPermissions.observe(this@MainActivity, { navigateTo(PermissionListFragment(), FramentTag.LocalPermissions) })
            openAbout.observe(this@MainActivity, { navigateTo(AboutFragment(), FramentTag.About) })
            openSettings.observe(this@MainActivity, { navigateTo(SettingsFragment(), FramentTag.Settings) })
            openPremium.observe(this@MainActivity, { navigateTo(PremiumFragment(), FramentTag.Premium) })
            openPromoDialog.observe(this@MainActivity, { PromoDialog().showPromoDialog(this@MainActivity) })
            openFeatureDialog.observe(this@MainActivity, { FeatureDialog().showFeatureDialog(this@MainActivity) })
            openOnboarding.observe(this@MainActivity, { this@MainActivity.startActivity(Intent(this@MainActivity, IntroActivity::class.java)) })
        }

        binding.viewModel = viewModel

        AdUtils.displayAd(ad_view, ad_view_container)
    }

    public override fun onPause() {
        if (AdUtils.isAdEnabled) ad_view?.pause()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        if (AdUtils.isAdEnabled) ad_view?.resume()
    }

    public override fun onDestroy() {
        if (AdUtils.isAdEnabled) ad_view?.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.main_activity_placeholder)
            val consumedInChildFragment = fragment is BackPressedListener && fragment.onBackPressed()

            if (!consumedInChildFragment) {
                navigation_view.setCheckedItem(R.id.nav_app_list)
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_list, menu)
        return true
    }

    private fun navigateTo(fragment: Fragment, tag: FramentTag) {
        if (supportFragmentManager.findFragmentByTag(tag.toString())?.isVisible == true) {
            return
        }

        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity_placeholder, fragment, tag.toString())
                .addToBackStack(tag.toString())
                .commit()
    }

    private fun placeAppListFragment() {
        navigation_view.setCheckedItem(R.id.nav_app_list)
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity_placeholder, MainAppListFragment(), FramentTag.AppList.toString())
                .commit()
    }

    private fun popToAppList() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        navigation_view.setCheckedItem(R.id.nav_app_list)
    }

}
