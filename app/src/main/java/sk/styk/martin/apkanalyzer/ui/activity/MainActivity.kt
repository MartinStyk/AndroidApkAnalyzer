package sk.styk.martin.apkanalyzer.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.about.AboutFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppListDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.localstatistics.LocalStatisticsFragment
import sk.styk.martin.apkanalyzer.ui.activity.permission.list.LocalPermissionsFragment
import sk.styk.martin.apkanalyzer.ui.activity.settings.SettingsFragment
import sk.styk.martin.apkanalyzer.util.FirstStartHelper
import com.google.android.gms.ads.AdListener



/**
 * @author Martin Styk
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        FirstStartHelper.execute(this)

        // only on first run redirect to default fragment
        if (savedInstanceState == null) {
            navigation_view.setCheckedItem(R.id.nav_app_list)
            supportFragmentManager.beginTransaction().replace(R.id.main_activity_placeholder, AppListDetailFragment()).commit()
        }

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")

        val adRequest = AdRequest.Builder()
                .addTestDevice("72FEA8FEF46331E756C654CF5C76557C")
                .addTestDevice("BF33B5A261F50A48670A239AF55828FF")
                .build()

        ad_view.loadAd(adRequest)
        ad_view.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                ad_view.visibility = View.VISIBLE
            }
        }
    }

    public override fun onPause() {
        ad_view?.pause()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        ad_view?.resume()
    }

    public override fun onDestroy() {
        ad_view?.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment: Fragment? = when (item.itemId) {
            R.id.nav_app_list -> AppListDetailFragment()
            R.id.nav_local_stats -> LocalStatisticsFragment()
            R.id.nav_local_permissions -> LocalPermissionsFragment()
            R.id.nav_about -> AboutFragment()
            R.id.nav_settings -> SettingsFragment()
            else -> throw IllegalStateException()
        }

        supportFragmentManager.beginTransaction().replace(R.id.main_activity_placeholder, fragment).commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
