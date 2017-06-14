package sk.styk.martin.apkanalyzer;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sk.styk.martin.apkanalyzer.R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(sk.styk.martin.apkanalyzer.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(sk.styk.martin.apkanalyzer.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, sk.styk.martin.apkanalyzer.R.string.navigation_drawer_open, sk.styk.martin.apkanalyzer.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(sk.styk.martin.apkanalyzer.R.id.nav_view);
        navigationView.setCheckedItem(sk.styk.martin.apkanalyzer.R.id.nav_camera);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(sk.styk.martin.apkanalyzer.R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        // only on first run redirect to default fragment
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DefaultFragment fragment = new DefaultFragment();
            fragmentManager.beginTransaction().replace(sk.styk.martin.apkanalyzer.R.id.first_container, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(sk.styk.martin.apkanalyzer.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(sk.styk.martin.apkanalyzer.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == sk.styk.martin.apkanalyzer.R.id.action_settings) {
            //TODO start settings activity gere
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == sk.styk.martin.apkanalyzer.R.id.nav_camera) {
            // Handle the camera action
            fragment = new DefaultFragment();
            fragmentManager.beginTransaction().replace(sk.styk.martin.apkanalyzer.R.id.first_container, fragment).commit();

        } else if (id == sk.styk.martin.apkanalyzer.R.id.nav_gallery) {
            fragment = new ItemListFragment();
            fragmentManager.beginTransaction().replace(sk.styk.martin.apkanalyzer.R.id.first_container, fragment).commit();

        } else if (id == sk.styk.martin.apkanalyzer.R.id.nav_slideshow) {

        } else if (id == sk.styk.martin.apkanalyzer.R.id.nav_manage) {

        } else if (id == sk.styk.martin.apkanalyzer.R.id.nav_share) {

        } else if (id == sk.styk.martin.apkanalyzer.R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(sk.styk.martin.apkanalyzer.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
