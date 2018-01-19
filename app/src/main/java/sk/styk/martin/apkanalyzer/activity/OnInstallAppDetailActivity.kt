package sk.styk.martin.apkanalyzer.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.file.ApkFilePicker

/**
 * @author Martin Styk
 */
class OnInstallAppDetailActivity : AppCompatActivity() {

    private var apkPath: String? = null
    private var intent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)
        val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar)
        val toolbar = findViewById<Toolbar>(R.id.detail_toolbar)
        setSupportActionBar(toolbar)

        if (getIntent().data == null) {
            Toast.makeText(this, getString(R.string.error_loading_package_detail), Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        intent = getIntent()
        apkPath = ApkFilePicker.getPathFromIntentData(intent!!, this)

        if (savedInstanceState == null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSIONS)
            } else {
                setupDetailFragment()
            }
        }

        val actionButton = findViewById<FloatingActionButton>(R.id.btn_actions)

        // this happens only in tablet mode when this activity is rotated from horizontal to vertical orientation
        if (actionButton == null) {
            appBarLayout.setExpanded(false)
        } else {
            actionButton.visibility = View.VISIBLE
            actionButton.setOnClickListener { view ->
                //delegate to fragment
                (supportFragmentManager.findFragmentByTag(AppDetailFragment.TAG) as AppDetailFragment).onClick(view)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSIONS) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupDetailFragment()
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun setupDetailFragment() {
        val arguments = Bundle()
        arguments.putString(AppDetailFragment.ARG_PACKAGE_PATH, apkPath)
        val detailFragment = AppDetailFragment()
        detailFragment.arguments = arguments
        supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, detailFragment, AppDetailFragment.TAG)
                .commitAllowingStateLoss()
    }

    companion object {

        private val REQUEST_STORAGE_PERMISSIONS = 987
    }

}
