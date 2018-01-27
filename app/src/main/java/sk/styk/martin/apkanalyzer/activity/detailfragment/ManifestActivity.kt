package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme
import kotlinx.android.synthetic.main.activity_manifest.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.AndroidManifestLoader
import sk.styk.martin.apkanalyzer.business.analysis.task.StringToFileSaveService

/**
 * @author Martin Styk
 * @version 15.09.2017.
 */
class ManifestActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    private var manifest: String = ""
    private lateinit var appPackageName: String

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manifest)

        appPackageName = intent.getStringExtra(PACKAGE_NAME_FOR_MANIFEST_REQUEST) ?: throw IllegalArgumentException("packageName not specified")

        code_view.highlightLanguage = Language.XML
        code_view.theme = Theme.ATOM_ONE_LIGHT

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportLoaderManager.initLoader(AndroidManifestLoader.ID, intent.extras, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<String> {
        return AndroidManifestLoader(this, args.getString(PACKAGE_NAME_FOR_MANIFEST_REQUEST)!!)
    }

    override fun onLoadFinished(loader: Loader<String>, data: String) {
        manifest = data
        code_view.setSource(manifest)
        code_loading.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<String>) {
        manifest = ""
        code_view.setSource("")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manifest_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> return saveWithPermissionCheck()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun saveWithPermissionCheck(): Boolean {
        if (manifest.isBlank()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.save_manifest_fail, Snackbar.LENGTH_SHORT)
            return false
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        } else {
            exportManifestFile()
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportManifestFile()
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun exportManifestFile() {
        val targetFile = StringToFileSaveService.startService(this, appPackageName, manifest)
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.save_manifest_background, targetFile), Snackbar.LENGTH_LONG).show()
    }

    companion object {

        const val PACKAGE_NAME_FOR_MANIFEST_REQUEST = "packageNameForManifestRequest"
        private const val REQUEST_STORAGE_PERMISSION = 11
    }
}
