package sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme
import kotlinx.android.synthetic.main.activity_manifest.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.AndroidManifestLoader
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestContract.Companion.PACKAGE_NAME_FOR_MANIFEST_REQUEST
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestContract.Companion.REQUEST_STORAGE_PERMISSION

/**
 * @author Martin Styk
 * @version 15.09.2017.
 */
class ManifestActivity : AppCompatActivity(), ManifestContract.View {

    private lateinit var presenter: ManifestContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_manifest)

        presenter = ManifestPresenter(AndroidManifestLoader(this, intent.getStringExtra(ManifestContract.PACKAGE_NAME_FOR_MANIFEST_REQUEST)), supportLoaderManager)
        presenter.view = this
        presenter.initialize(intent.getStringExtra(ManifestContract.PACKAGE_NAME_FOR_MANIFEST_REQUEST))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manifest_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {presenter.saveManifestWithPermissionCheck(); true}
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun setUpViews() {
        code_view.highlightLanguage = Language.XML
        code_view.theme = Theme.ATOM_ONE_LIGHT

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun hideLoading() {
        code_loading.visibility = View.GONE
    }

    override fun showManifest(manifest: String) {
        code_view.setSource(manifest)
    }

    override fun makeSnackbar(stringId: Int, param: String?) {
        if (param == null)
            Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_SHORT).show()
        else
            Snackbar.make(findViewById(android.R.id.content), getString(stringId, param), Snackbar.LENGTH_SHORT).show()
    }

    override fun askForStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        } else {
            presenter.saveManifest()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.saveManifest()
            } else {
                makeSnackbar(R.string.permission_not_granted)
            }
        }
    }

    companion object {

        fun createIntent(context: Context, packageName: String): Intent {
            val intent = Intent(context, ManifestActivity::class.java)
            intent.putExtra(PACKAGE_NAME_FOR_MANIFEST_REQUEST, packageName);
            return intent
        }
    }
}
