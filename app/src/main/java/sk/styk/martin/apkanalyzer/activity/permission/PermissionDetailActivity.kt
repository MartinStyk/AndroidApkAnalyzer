package sk.styk.martin.apkanalyzer.activity.permission

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_detail)

        val permissionData = intent.getParcelableExtra<LocalPermissionData>(ARG_PERMISSIONS_DATA)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = permissionData.permissionData.simpleName
        }


        if (savedInstanceState == null) {
            val arguments = Bundle()
            arguments.putParcelable(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA, permissionData)
            val detailFragment = PermissionDetailPagerFragment()
            detailFragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, detailFragment, PermissionDetailPagerFragment.TAG)
                    .commit()
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
