package sk.styk.martin.apkanalyzer.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

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

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        if (FirstStartHelper.isFirstStart(this)) {
            startActivity(Intent(this, IntroActivity::class.java))
        }

        // only on first run redirect to default fragment
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_app_list)
            supportFragmentManager.beginTransaction().replace(R.id.main_activity_placeholder, AnalyzeFragment()).commit()
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        var fragment: Fragment? = null
        if (id == R.id.nav_app_list) {
            fragment = AnalyzeFragment()
        } else if (id == R.id.nav_local_stats) {
            fragment = LocalStatisticsFragment()
        } else if (id == R.id.nav_local_permissions) {
            fragment = LocalPermissionsFragment()
        } else if (id == R.id.nav_about) {
            fragment = AboutFragment()
        } else if (id == R.id.nav_settings) {
            fragment = SettingsFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.main_activity_placeholder, fragment).commit()

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
