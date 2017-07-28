package sk.styk.martin.apkanalyzer.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
 * List of all applications
 */
public class AppListFragment extends ListFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<List<AppListData>> {

    private AppListAdapter listAdapter;

    private SearchView searchView;

    private String currentFilter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState == null) {
            setEmptyText(getResources().getString(R.string.app_list_empty));

            setHasOptionsMenu(true);

            listAdapter = new AppListAdapter(getActivity());
            setListAdapter(listAdapter);

            setListShown(false);

            getLoaderManager().initLoader(AppListLoader.ID, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        searchView = new SearchView(getActivity());
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setIconifiedByDefault(true);

        searchView.setBackgroundColor(Color.WHITE);

        item.setActionView(searchView);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        currentFilter = !TextUtils.isEmpty(newText) ? newText : null;
        listAdapter.getFilter().filter(currentFilter);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(searchView.getQuery())) {
            searchView.setQuery(null, true);
        }
        return true;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        AnalyzeFragment parentFragment = (AnalyzeFragment) getParentFragment();
        AppListData appBasicData = AppListData.class.cast(view.getTag());
        parentFragment.itemClicked(appBasicData.getPackageName());
    }

    @Override
    public Loader<List<AppListData>> onCreateLoader(int id, Bundle args) {
        return new AppListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<AppListData>> loader, List<AppListData> data) {
        listAdapter.setData(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<AppListData>> loader) {
        listAdapter.setData(null);
    }

}
