package sk.styk.martin.apkanalyzer.ui.activity.permission.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager.PermissionDetailPagerFragment
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager.PermissionDetailPagerFragment.Companion.ARG_PERMISSIONS_DATA

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_detail)

        val permissionData = intent.getParcelableExtra<LocalPermissionData>(ARG_PERMISSIONS_DATA)
                ?: throw IllegalArgumentException("data null")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = permissionData.permissionData.simpleName


        if (savedInstanceState == null) {
            val fragment = PermissionDetailPagerFragment.create(permissionData)
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment, PermissionDetailPagerFragment.TAG)
                    .commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
