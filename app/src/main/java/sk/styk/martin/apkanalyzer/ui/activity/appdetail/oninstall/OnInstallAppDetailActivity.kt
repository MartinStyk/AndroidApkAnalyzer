package sk.styk.martin.apkanalyzer.ui.activity.appdetail.oninstall

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_app_detail.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerFragment

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
        }
    }

    override fun errorLoading() {
        Toast.makeText(this, getString(R.string.error_loading_package_detail), Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                    .add(R.id.item_detail_container, AppDetailPagerFragment.create(packageUri = presenter.packageUri), AppDetailPagerFragment.TAG)
                    .commitAllowingStateLoss()
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSIONS = 987
    }

}
