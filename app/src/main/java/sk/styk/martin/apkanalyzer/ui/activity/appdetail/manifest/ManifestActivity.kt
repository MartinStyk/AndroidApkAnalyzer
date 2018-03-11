package sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme
import kotlinx.android.synthetic.main.activity_manifest.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.AndroidManifestLoader
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestContract.Companion.PACKAGE_NAME_FOR_MANIFEST_REQUEST

/**
 * @author Martin Styk
 * @version 15.09.2017.
 */
@RuntimePermissions
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
        R.id.action_save -> {
            saveManifestWithPermissionCheck(); true
        }
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

    override fun makeSnackbar (text: String, @StringRes actionName: Int?, action: View.OnClickListener?) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
        if (action != null && actionName != null)
            snackbar.setAction(actionName, action)
        snackbar.show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun saveManifest() {
        presenter.saveManifest()
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onStorageDenied() {
        makeSnackbar(getString(R.string.permission_not_granted))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    companion object {

        fun createIntent(context: Context, packageName: String): Intent {
            val intent = Intent(context, ManifestActivity::class.java)
            intent.putExtra(PACKAGE_NAME_FOR_MANIFEST_REQUEST, packageName)
            return intent
        }
    }
}
