package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar

import com.pddstudio.highlightjs.HighlightJsView
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.task.AndroidManifestLoader
import sk.styk.martin.apkanalyzer.business.task.StringToFileSaveService

/**
 * @author Martin Styk
 * @version 15.09.2017.
 */
class ManifestActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    private var codeView: HighlightJsView? = null
    private var loadingBar: ProgressBar? = null

    private var manifest: String? = null
    private var packageName: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manifest)

        packageName = intent.getStringExtra(PACKAGE_NAME_FOR_MANIFEST_REQUEST)

        codeView = findViewById(R.id.code_view)
        codeView!!.highlightLanguage = Language.XML
        codeView!!.theme = Theme.ATOM_ONE_LIGHT

        loadingBar = findViewById(R.id.code_loading)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        supportLoaderManager.initLoader(AndroidManifestLoader.ID, intent.extras, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<String> {
        return AndroidManifestLoader(this, args.getString(PACKAGE_NAME_FOR_MANIFEST_REQUEST)!!)
    }

    override fun onLoadFinished(loader: Loader<String>, data: String) {
        manifest = data
        codeView!!.setSource(manifest)
        loadingBar!!.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<String>) {
        manifest = ""
        codeView!!.setSource("")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manifest_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> return saveWithPermissionCheck()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun saveWithPermissionCheck(): Boolean {
        if (manifest == null || manifest!!.isEmpty()) {
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
        val targetFile = StringToFileSaveService.startService(this, packageName!!, manifest!!)
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.save_manifest_background, targetFile), Snackbar.LENGTH_LONG).show()
    }

    companion object {

        val PACKAGE_NAME_FOR_MANIFEST_REQUEST = "packageNameForManifestRequest"
        private val REQUEST_STORAGE_PERMISSION = 11
    }
}
