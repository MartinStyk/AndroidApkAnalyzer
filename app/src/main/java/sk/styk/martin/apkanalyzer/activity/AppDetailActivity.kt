package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_app_detail.*
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
        setContentView(R.layout.activity_app_detail)
        setSupportActionBar(detail_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        // this happens only in tablet mode when this activity is rotated from horizontal to vertical orientation
        if (btn_actions == null) {
            app_bar.setExpanded(false)
        } else {
            btn_actions!!.setOnClickListener { view ->
                //delegate to fragment
                (supportFragmentManager.findFragmentByTag(AppDetailFragment.TAG) as AppDetailFragment).onClick(view)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

