package sk.styk.martin.apkanalyzer.activity;

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

import sk.styk.martin.apkanalyzer.R;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AppListFragment}.
 *
 * @author Martin Styk
 */
public class AppDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sk.styk.martin.apkanalyzer.R.layout.activity_app_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(AppDetailFragment.ARG_PACKAGE_NAME, getIntent().getStringExtra(AppDetailFragment.ARG_PACKAGE_NAME));
            arguments.putString(AppDetailFragment.ARG_PACKAGE_PATH, getIntent().getStringExtra(AppDetailFragment.ARG_PACKAGE_PATH));
            Fragment detailFragment = new AppDetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(sk.styk.martin.apkanalyzer.R.id.item_detail_container, detailFragment, AppDetailFragment.TAG)
                    .commit();
        }

        FloatingActionButton actionButton = findViewById(R.id.btn_actions);

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
