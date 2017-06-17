package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private ProgressBar loadingBar;
    private View dataContainer;
    private CollapsingToolbarLayout appBarLayout;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(sk.styk.martin.apkanalyzer.R.layout.fragment_app_detail, container, false);

        loadingBar = (ProgressBar) rootView.findViewById(R.id.item_detail_loading);
        dataContainer = rootView.findViewById(R.id.item_detail_container);
        textView = (TextView) rootView.findViewById(R.id.item_detail);
        appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(sk.styk.martin.apkanalyzer.R.id.toolbar_layout);

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
        }
        loadingBar.setVisibility(View.GONE);
        dataContainer.setVisibility(View.VISIBLE);
        textView.setText(data.toString());
    }

    @Override
    public void onLoaderReset(Loader<AppBasicInfo> loader) {
        this.data = null;
    }

}
