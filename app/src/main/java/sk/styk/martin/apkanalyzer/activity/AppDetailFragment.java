package sk.styk.martin.apkanalyzer.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.AppDetailLoader;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AppListActivity}
 * in two-pane mode (on tablets) or a {@link AppDetailActivity}
 * on handsets.
 */
public class AppDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<AppBasicInfo>,
        BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private AppBasicInfo data;

    private CollapsingToolbarLayout appBarLayout;
    private ImageView appBarLayuotImageView;

    private AppDetailAdapter adapter;
    private ProgressBar loadingBar;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AppDetailAdapter(getFragmentManager());
        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(sk.styk.martin.apkanalyzer.R.layout.fragment_app_detail, container, false);

        loadingBar = (ProgressBar) rootView.findViewById(R.id.item_detail_loading);
        appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(sk.styk.martin.apkanalyzer.R.id.toolbar_layout);
        appBarLayuotImageView = (ImageView) getActivity().findViewById(R.id.toolbar_layout_image);

        bottomNavigationView = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);

        if (data != null) {
            onLoadFinished(null, data);
        }

        return rootView;
    }

    @Override
    public Loader<AppBasicInfo> onCreateLoader(int id, Bundle args) {
        return new AppDetailLoader(getActivity(), args.getInt(ARG_ITEM_ID));
    }

    @Override
    public void onLoadFinished(Loader<AppBasicInfo> loader, AppBasicInfo data) {
        this.data = data;
        if (appBarLayout != null) {
            appBarLayout.setTitle(data.getPackageName());
            appBarLayuotImageView.setImageDrawable(data.getIcon());
        }
        loadingBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<AppBasicInfo> loader) {
        this.data = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_item1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.action_item2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.action_item3:
                viewPager.setCurrentItem(2);
                break;
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // nothing to de here
    }

    @Override
    public void onPageSelected(int position) {
        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        } else {
            bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevMenuItem = bottomNavigationView.getMenu().getItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // nothing to de here
    }
}
