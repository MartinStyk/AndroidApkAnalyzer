package sk.styk.martin.apkanalyzer.activity;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.SearchView;
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
import sk.styk.martin.apkanalyzer.model.list.AppListData;

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
        // Show an action bar item for searching.
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setEnabled(true).setVisible(true);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
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
