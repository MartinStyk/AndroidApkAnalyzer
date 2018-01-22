package sk.styk.martin.apkanalyzer.activity.permission

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailPagerFragment.Companion.ARG_PERMISSIONS_DATA
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

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
