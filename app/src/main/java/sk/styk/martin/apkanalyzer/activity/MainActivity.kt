package sk.styk.martin.apkanalyzer.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.intro.IntroActivity
import sk.styk.martin.apkanalyzer.activity.permission.LocalPermissionsFragment
import sk.styk.martin.apkanalyzer.util.FirstStartHelper

/**
 * @author Martin Styk
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        if (FirstStartHelper.isFirstStart(this)) {
            startActivity(Intent(this, IntroActivity::class.java))
        }

        // only on first run redirect to default fragment
        if (savedInstanceState == null) {
            navigation_view.setCheckedItem(R.id.nav_app_list)
            supportFragmentManager.beginTransaction().replace(R.id.main_activity_placeholder, AnalyzeFragment()).commit()
        }
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
        val id = item.itemId

        var fragment: Fragment? = null
        when (id) {
            R.id.nav_app_list -> fragment = AnalyzeFragment()
            R.id.nav_local_stats -> fragment = LocalStatisticsFragment()
            R.id.nav_local_permissions -> fragment = LocalPermissionsFragment()
            R.id.nav_about -> fragment = AboutFragment()
            R.id.nav_settings -> fragment = SettingsFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.main_activity_placeholder, fragment).commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
