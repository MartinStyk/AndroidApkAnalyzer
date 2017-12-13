package sk.styk.martin.apkanalyzer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.intro.IntroActivity;
import sk.styk.martin.apkanalyzer.util.FirstStartHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (FirstStartHelper.check(this)) {
            startActivity(new Intent(this, IntroActivity.class));
        }

        // only on first run redirect to default fragment
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_app_list);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_placeholder, new AnalyzeFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_app_list) {
            fragment = new AnalyzeFragment();
        } else if (id == R.id.nav_local_stats) {
            fragment = new LocalStatisticsFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_placeholder, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
