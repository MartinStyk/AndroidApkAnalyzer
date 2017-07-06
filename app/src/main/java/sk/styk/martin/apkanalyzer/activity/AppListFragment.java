package sk.styk.martin.apkanalyzer.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.adapter.AppListAdapter;
import sk.styk.martin.apkanalyzer.business.task.AppListLoader;
import sk.styk.martin.apkanalyzer.model.AppListData;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppListFragment extends ListFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<List<AppListData>> {

    // This is the Adapter being used to display the list's data.
    private AppListAdapter mAdapter;

    // The SearchView for doing filtering.
    private SearchView mSearchView;

    // If non-null, this is the current filter the user has provided.
    private String mCurFilter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState == null) {
            // Give some text to display if there is no data.  In a real
            // application this would come from a resource.
            setEmptyText(getResources().getString(R.string.app_list_empty));

            // We have a menu item to show in action bar.
            setHasOptionsMenu(true);

            // Create an empty adapter we will use to display the loaded data.
            mAdapter = new AppListAdapter(getActivity());
            setListAdapter(mAdapter);

            // Start out with a progress indicator.
            setListShown(false);

            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        mSearchView = new SearchView(getActivity());
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setIconifiedByDefault(true);
        item.setActionView(mSearchView);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Since this
        // is a simple array adapter, we can just have it do the filtering.
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        mAdapter.getFilter().filter(mCurFilter);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        return true;
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        AppListData appBasicData = AppListData.class.cast(view.getTag());
        if (!MainActivity.mTwoPane) {
            Context context = view.getContext();
            Intent intent = new Intent(context, AppDetailActivity.class);
            intent.putExtra(AppDetailFragment.ARG_PACKAGE_NAME, appBasicData.getPackageName());
            intent.putExtra(AppDetailFragment.ARG_ARCHIVE_PATH, appBasicData.getPathToApk());
            context.startActivity(intent);
        } else {
            Bundle arguments = new Bundle();
            arguments.putString(AppDetailFragment.ARG_PACKAGE_NAME, appBasicData.getPackageName());
            arguments.putString(AppDetailFragment.ARG_ARCHIVE_PATH, appBasicData.getPathToApk());

            AppDetailFragment fragment = new AppDetailFragment();
            fragment.setArguments(arguments);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
        }
    }

    @Override
    public Loader<List<AppListData>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader with no arguments, so it is simple.
        return new AppListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<AppListData>> loader, List<AppListData> data) {
        // Set the new data in the adapter.
        mAdapter.setData(data);

        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<AppListData>> loader) {
        mAdapter.setData(null);
    }

}
