package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
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
public class AppDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<AppBasicInfo> {
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

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


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
    }

    @Override
    public void onLoaderReset(Loader<AppBasicInfo> loader) {
        this.data = null;
    }

}
