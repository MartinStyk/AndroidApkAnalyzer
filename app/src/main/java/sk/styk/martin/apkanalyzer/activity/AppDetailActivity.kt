package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import sk.styk.martin.apkanalyzer.R

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [AppListFragment].
 *
 * @author Martin Styk
 */
class AppDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(sk.styk.martin.apkanalyzer.R.layout.activity_app_detail)
        val toolbar = findViewById<Toolbar>(R.id.detail_toolbar)
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar)

        if (savedInstanceState == null) {

            val arguments = Bundle()
            arguments.putString(AppDetailFragment.ARG_PACKAGE_NAME, intent.getStringExtra(AppDetailFragment.ARG_PACKAGE_NAME))
            arguments.putString(AppDetailFragment.ARG_PACKAGE_PATH, intent.getStringExtra(AppDetailFragment.ARG_PACKAGE_PATH))
            val detailFragment = AppDetailFragment()
            detailFragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .add(sk.styk.martin.apkanalyzer.R.id.item_detail_container, detailFragment, AppDetailFragment.TAG)
                    .commit()
        }

        val actionButton = findViewById<FloatingActionButton>(R.id.btn_actions)

        // this happens only in tablet mode when this activity is rotated from horizontal to vertical orientation
        if (actionButton == null) {
            appBarLayout.setExpanded(false)
        } else {
            actionButton.setOnClickListener { view ->
                //delegate to fragment
                (supportFragmentManager.findFragmentByTag(AppDetailFragment.TAG) as AppDetailFragment).onClick(view)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
