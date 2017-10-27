package sk.styk.martin.apkanalyzer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.util.ApkFilePicker;

public class OnInstallAppDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        if(getIntent().getData() == null){
            Toast.makeText(this, "Can not get APK data.", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(AppDetailFragment.ARG_PACKAGE_PATH, ApkFilePicker.getPathFromIntentData(getIntent(), this));
            Fragment detailFragment = new AppDetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, detailFragment, AppDetailFragment.TAG)
                    .commit();
        }

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.btn_actions);

        // this happens only in tablet mode when this activity is rotated from horizontal to vertical orientation
        if (actionButton == null) {
            appBarLayout.setExpanded(false);
        } else {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, AppListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
