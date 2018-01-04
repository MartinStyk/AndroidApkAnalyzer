package sk.styk.martin.apkanalyzer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.util.file.ApkFilePicker;

public class OnInstallAppDetailActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSIONS = 987;

    private String apkPath;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getData() == null) {
            Toast.makeText(this, getString(R.string.error_loading_package_detail), Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        intent = getIntent();
        apkPath = ApkFilePicker.getPathFromIntentData(intent, this);

        if (savedInstanceState == null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSIONS);
            } else {
                setupDetailFragment();
            }
        }

        FloatingActionButton actionButton = findViewById(R.id.btn_actions);

        // this happens only in tablet mode when this activity is rotated from horizontal to vertical orientation
        if (actionButton == null) {
            appBarLayout.setExpanded(false);
        } else {
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delegate to fragment
                    ((AppDetailFragment) getSupportFragmentManager().findFragmentByTag(AppDetailFragment.TAG)).onClick(view);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupDetailFragment();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void setupDetailFragment() {
        Bundle arguments = new Bundle();
        arguments.putString(AppDetailFragment.ARG_PACKAGE_PATH, apkPath);
        Fragment detailFragment = new AppDetailFragment();
        detailFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_detail_container, detailFragment, AppDetailFragment.TAG)
                .commitAllowingStateLoss();
    }

}
