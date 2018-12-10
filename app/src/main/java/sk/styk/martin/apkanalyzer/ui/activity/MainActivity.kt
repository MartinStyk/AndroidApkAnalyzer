package sk.styk.martin.apkanalyzer.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.about.AboutFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppListDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.dialog.FeatureDialog
import sk.styk.martin.apkanalyzer.ui.activity.dialog.PromoDialog
import sk.styk.martin.apkanalyzer.ui.activity.localstatistics.LocalStatisticsFragment
import sk.styk.martin.apkanalyzer.ui.activity.permission.list.LocalPermissionsFragment
import sk.styk.martin.apkanalyzer.ui.activity.premium.PremiumFragment
import sk.styk.martin.apkanalyzer.ui.activity.settings.SettingsFragment
import sk.styk.martin.apkanalyzer.util.AdUtils
import sk.styk.martin.apkanalyzer.util.AppFlavour
import sk.styk.martin.apkanalyzer.util.BackPressedListener
import sk.styk.martin.apkanalyzer.util.StartPromoHelper


/**
 * @author Martin Styk
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, PromoDialog.PromoDialogController, FeatureDialog.FeatureDialogController {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        StartPromoHelper.execute(this)

        // only on first run redirect to default fragment
        if (savedInstanceState == null) {
            navigation_view.setCheckedItem(R.id.nav_app_list)
            NavigationFragmentWrapper.AppListDetail.navigateToFragment(supportFragmentManager)
        }
        if (AppFlavour.isPremium) {
            navigation_view?.menu?.findItem(R.id.nav_premium)?.isVisible = false
        }

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

            if(!consumedInChildFragment){
                if (!NavigationFragmentWrapper.AppListDetail.isVisible(supportFragmentManager)) {
                    super.onBackPressed()
                    navigation_view.setCheckedItem(NavigationFragmentWrapper.currentlyDisplayedFragment(supportFragmentManager).navigationId)
                } else {
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment = NavigationFragmentWrapper.findFragment(navigationId = item.itemId)

        if (!fragment.isVisible(supportFragmentManager)) {
            fragment.navigateToFragment(supportFragmentManager)
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPromoDialogShowRequested() = PromoDialog().showPromoDialog(this)

    override fun onFeatureDialogShowRequested() = FeatureDialog().showFeatureDialog(this)

    sealed class NavigationFragmentWrapper(
            @IdRes val navigationId: Int,
            val tag: String,
            val factory: () -> Fragment
    ) {
        companion object {
            fun findFragment(@IdRes navigationId: Int) =
                    listOf(AppListDetail, LocalStatistics, LocalPermissions, About, Settings, Premium).first { it.navigationId == navigationId }

            fun currentlyDisplayedFragment(fragmentManager: FragmentManager) =
                    listOf(AppListDetail, LocalStatistics, LocalPermissions, About, Settings, Premium).first { fragmentManager.findFragmentByTag(it.tag)?.isVisible == true }

        }

        fun isVisible(fragmentManager: FragmentManager) = fragmentManager.findFragmentByTag(tag)?.isVisible == true

        fun navigateToFragment(fragmentManager: FragmentManager) = fragmentManager.beginTransaction()
                .replace(R.id.main_activity_placeholder, factory.invoke(), tag)
                .addToBackStack(tag)
                .commit()

        object AppListDetail : NavigationFragmentWrapper(R.id.nav_app_list, "app_detail", { AppListDetailFragment() })
        object LocalStatistics : NavigationFragmentWrapper(R.id.nav_local_stats, "local_stat", { LocalStatisticsFragment() })
        object LocalPermissions : NavigationFragmentWrapper(R.id.nav_local_permissions, "local_permissions", { LocalPermissionsFragment() })
        object About : NavigationFragmentWrapper(R.id.nav_about, "about", { AboutFragment() })
        object Settings : NavigationFragmentWrapper(R.id.nav_settings, "settings", { SettingsFragment() })
        object Premium : NavigationFragmentWrapper(R.id.nav_premium, "premium", { PremiumFragment() })

    }
}
