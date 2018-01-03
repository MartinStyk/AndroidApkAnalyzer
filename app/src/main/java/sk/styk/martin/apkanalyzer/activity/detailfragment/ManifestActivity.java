package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;

import java.io.File;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.AndroidManifestLoader;
import sk.styk.martin.apkanalyzer.business.task.FileCopyService;
import sk.styk.martin.apkanalyzer.business.task.StringToFileSaveService;

/**
 * Created by Martin Styk on 15.09.2017.
 */
public class ManifestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String PACKAGE_NAME_FOR_MANIFEST_REQUEST = "packageNameForManifestRequest";
    private static final int REQUEST_STORAGE_PERMISSION = 11;

    private HighlightJsView codeView;
    private ProgressBar loadingBar;

    private String manifest;
    private String packageName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manifest);

        packageName = getIntent().getStringExtra(PACKAGE_NAME_FOR_MANIFEST_REQUEST);

        codeView = findViewById(R.id.code_view);
        codeView.setHighlightLanguage(Language.XML);
        codeView.setTheme(Theme.ATOM_ONE_LIGHT);

        loadingBar = findViewById(R.id.code_loading);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportLoaderManager().initLoader(AndroidManifestLoader.ID, getIntent().getExtras(), this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AndroidManifestLoader(this, args.getString(PACKAGE_NAME_FOR_MANIFEST_REQUEST));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        manifest = data;
        codeView.setSource(manifest);
        loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        manifest = "";
        codeView.setSource("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manifest_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                return saveWithPermissionCheck();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean saveWithPermissionCheck() {
        if (manifest == null || manifest.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.save_manifest_fail, Snackbar.LENGTH_SHORT);
            return false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            exportManifestFile();
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportManifestFile();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void exportManifestFile() {
        File target = new File(Environment.getExternalStorageDirectory(), "AndroidManifest_" + packageName + ".xml");

        Intent intent = new Intent(this, StringToFileSaveService.class);
        intent.putExtra(StringToFileSaveService.SOURCE_STRING, manifest);
        intent.putExtra(StringToFileSaveService.TARGET_FILE, target.getAbsolutePath());

        ContextCompat.startForegroundService(this, intent);

        Snackbar.make(findViewById(android.R.id.content), getString(R.string.save_manifest_background, target.getAbsolutePath()), Snackbar.LENGTH_LONG).show();
    }
}
