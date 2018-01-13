package sk.styk.martin.apkanalyzer.activity.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.activity.AppListActivity;
import sk.styk.martin.apkanalyzer.model.detail.PermissionData;
import sk.styk.martin.apkanalyzer.model.permissions.PermissionStatus;

import static sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailFragment.ARG_PERMISSIONS_DATA;

public class PermissionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();

            arguments.putParcelable(PermissionDetailFragment.ARG_PERMISSIONS_DATA, getIntent().getParcelableExtra(ARG_PERMISSIONS_DATA));
            Fragment detailFragment = new PermissionDetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, detailFragment, PermissionDetailFragment.TAG)
                    .commit();
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
