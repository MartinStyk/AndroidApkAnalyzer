package sk.styk.martin.apkanalyzer.ui.activity.appdetail.oninstall

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_app_detail.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerFragment

/**
 * @author Martin Styk
 */
class OnInstallAppDetailActivity : AppCompatActivity(), OnInstallAppDetailContract.View {

    private lateinit var presenter: OnInstallAppDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        presenter = OnInstallAppDetailPresenter()
        presenter.view = this
        presenter.initialize(intent?.data, this)
    }

    override fun setupViews() {
        setSupportActionBar(detail_toolbar)
        // this happens only in tablet mode when this activity is rotated from horizontal to vertical orientation
        if (btn_actions == null) {
            app_bar.setExpanded(false)
        } else {
            btn_actions!!.visibility = View.VISIBLE
            btn_actions!!.setOnClickListener { _ ->
                //delegate to fragment
                (supportFragmentManager.findFragmentByTag(AppDetailPagerFragment.TAG) as AppDetailPagerFragment).presenter.actionButtonClick()
            }
        }
    }

    override fun errorLoading() {
        Toast.makeText(this, getString(R.string.error_loading_package_detail), Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSIONS)
        } else {
            presenter.storagePermissionGranted()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.storagePermissionGranted()
            } else {
                presenter.storagePermissionRefused()
            }
        }
    }

    override fun onPermissionRefused() {
        Snackbar.make(findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show()
        finish()
    }

    override fun setupDetailFragment() {

        val fragment = supportFragmentManager.findFragmentByTag(AppDetailPagerFragment.TAG)
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, AppDetailPagerFragment.create(packagePath = presenter.packagePath), AppDetailPagerFragment.TAG)
                    .commitAllowingStateLoss()
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSIONS = 987
    }

}
