package sk.styk.martin.apkanalyzer.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialFade
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ActivityMainBinding
import sk.styk.martin.apkanalyzer.manager.promo.UserReviewManager
import sk.styk.martin.apkanalyzer.ui.ApkAnalyzerBaseActivity
import sk.styk.martin.apkanalyzer.ui.about.AboutFragment
import sk.styk.martin.apkanalyzer.ui.applist.main.MainAppListFragment
import sk.styk.martin.apkanalyzer.ui.dialogs.PromoDialog
import sk.styk.martin.apkanalyzer.ui.intro.IntroActivity
import sk.styk.martin.apkanalyzer.ui.permission.list.PermissionListFragment
import sk.styk.martin.apkanalyzer.ui.premium.PremiumFragment
import sk.styk.martin.apkanalyzer.ui.settings.SettingsFragment
import sk.styk.martin.apkanalyzer.ui.statistics.StatisticsFragment
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.util.onViewLaidOut
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ApkAnalyzerBaseActivity() {

    @Inject
    lateinit var factory: MainActivityViewModel.Factory

    @Inject
    lateinit var userReviewManager: UserReviewManager

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModel = provideViewModel { factory.create() }
        with(viewModel) {
            closeDrawer.observe(this@MainActivity) { binding.drawerLayout.closeDrawer(GravityCompat.START) }
            openDrawer.observe(this@MainActivity) { binding.drawerLayout.openDrawer(GravityCompat.START) }
            premiumMenuItemVisible.observe(this@MainActivity) {
                binding.navigationView.onViewLaidOut {
                    binding.navigationView.menu.findItem(R.id.nav_premium).isVisible = it
                }
            }
            placeInitialFragment.observe(this@MainActivity) { placeAppListFragment() }
            openAppList.observe(this@MainActivity) { popToAppList() }
            openStatistics.observe(this@MainActivity) {
                navigateTo(
                    StatisticsFragment(),
                    FragmentTag.LocalStatistics,
                )
            }
            openPermissions.observe(this@MainActivity) {
                navigateTo(
                    PermissionListFragment(),
                    FragmentTag.LocalPermissions,
                )
            }
            openAbout.observe(this@MainActivity) { navigateTo(AboutFragment(), FragmentTag.About) }
            openSettings.observe(this@MainActivity) {
                navigateTo(
                    SettingsFragment(),
                    FragmentTag.Settings,
                )
            }
            openPremium.observe(this@MainActivity) {
                navigateTo(
                    PremiumFragment(),
                    FragmentTag.Premium,
                )
            }
            openPromoDialog.observe(this@MainActivity) { PromoDialog().showPromoDialog(this@MainActivity) }
            openOnboarding.observe(this@MainActivity) {
                this@MainActivity.startActivity(
                    Intent(
                        this@MainActivity,
                        IntroActivity::class.java,
                    ),
                )
            }
            selectedMenuItem.observe(this@MainActivity) { binding.navigationView.setCheckedItem(it) }
        }

        binding.viewModel = viewModel
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun navigateTo(fragment: Fragment, tag: FragmentTag) {
        if (supportFragmentManager.findFragmentByTag(tag.toString())?.isVisible == true) {
            return
        }

        supportFragmentManager.findFragmentById(R.id.container)?.let {
            it.enterTransition = null
            it.reenterTransition = null
            it.exitTransition = null
            it.enterTransition = MaterialFade()
        }

        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag.toString())
            .setReorderingAllowed(true)
            .addToBackStack(tag.toString())
            .commit()
    }

    private fun placeAppListFragment() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container, MainAppListFragment(), FragmentTag.AppList.tag)
            .commit()
    }

    private fun popToAppList() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
