package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.AndroidManifestLoader;

/**
 * Created by Martin Styk on 15.09.2017.
 */
public class ManifestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String PACKAGE_NAME_FOR_MANIFEST_REQUEST = "packageNameForManifestRequest";

    private TextView codeView;
    private ProgressBar loadingBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manifest);

        codeView = (TextView) findViewById(R.id.code_view);
        loadingBar = (ProgressBar) findViewById(R.id.code_loading);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportLoaderManager().initLoader(AndroidManifestLoader.ID, getIntent().getExtras(), this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AndroidManifestLoader(this, args.getString(PACKAGE_NAME_FOR_MANIFEST_REQUEST));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        codeView.setText(data);
        loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        codeView.setText("");
    }
}
