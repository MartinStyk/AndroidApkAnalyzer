package sk.styk.martin.apkanalyzer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            //delegate to fragment
            val detailFragment = AppDetailFragment.create(packageName = intent.getStringExtra(AppDetailFragment.ARG_PACKAGE_NAME),
                    packagePath = intent.getStringExtra(AppDetailFragment.ARG_PACKAGE_PATH))

            supportFragmentManager.beginTransaction().
                    add(sk.styk.martin.apkanalyzer.R.id.item_detail_container, detailFragment, AppDetailFragment.TAG)
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


    companion object {
        @JvmStatic
        fun createIntent(packageName: String? = null, packagePath: String? = null, context: Context): Intent {
            val intent = Intent(context, AppDetailActivity::class.java)
            intent.putExtra(AppDetailFragment.ARG_PACKAGE_NAME, packageName)
            intent.putExtra(AppDetailFragment.ARG_PACKAGE_PATH, packagePath)

            return intent
        }
    }
}

