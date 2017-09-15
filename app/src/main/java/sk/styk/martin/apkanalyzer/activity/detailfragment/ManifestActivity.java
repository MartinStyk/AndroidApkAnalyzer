package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private TextView codeView;
    private ProgressBar loadingBar;

    private String manifest;
    private String packageName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manifest);

        packageName = getIntent().getStringExtra(PACKAGE_NAME_FOR_MANIFEST_REQUEST);

        codeView = (TextView) findViewById(R.id.code_view);
        loadingBar = (ProgressBar) findViewById(R.id.code_loading);

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
        codeView.setText(manifest);
        loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        manifest = "";
        codeView.setText(manifest);
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
                return saveManifestToFile();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean saveManifestToFile() {
        if (manifest == null || manifest.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.save_manifest_fail, Snackbar.LENGTH_SHORT);
            return false;
        }

        File target = new File(Environment.getExternalStorageDirectory(), "AndroidManifest_" + packageName + ".xml");

        Intent intent = new Intent(this, StringToFileSaveService.class);
        intent.putExtra(StringToFileSaveService.SOURCE_STRING, manifest);
        intent.putExtra(FileCopyService.TARGET_FILE, target.getAbsolutePath());

        startService(intent);

        Snackbar.make(findViewById(android.R.id.content), getString(R.string.save_manifest_background, target.getAbsolutePath()), Snackbar.LENGTH_LONG).show();

        return true;
    }
}
