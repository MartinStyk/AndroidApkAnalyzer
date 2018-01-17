package sk.styk.martin.apkanalyzer.activity.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppListActivity;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData;

import static sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA;

public class PermissionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_detail);

        LocalPermissionData permissionData = getIntent().getParcelableExtra(ARG_PERMISSIONS_DATA);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(permissionData.getPermissionData().getSimpleName());
        }


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA, permissionData);
            Fragment detailFragment = new PermissionDetailPagerFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, detailFragment, PermissionDetailPagerFragment.TAG)
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
