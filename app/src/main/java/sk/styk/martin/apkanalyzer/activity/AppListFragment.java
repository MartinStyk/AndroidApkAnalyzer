package sk.styk.martin.apkanalyzer.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.adapter.AppListAdapter;
import sk.styk.martin.apkanalyzer.business.task.AppListLoader;
import sk.styk.martin.apkanalyzer.model.list.AppListData;
import sk.styk.martin.apkanalyzer.util.ApkFilePicker;

import static android.app.Activity.RESULT_OK;

/**
 * List of all applications
 */
public class AppListFragment extends ListFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<AppListData>> {

    private static final int REQUESTCODE_STORAGE_PERMISSIONS = 13245;

    private AppListAdapter listAdapter;

    private SearchView searchView;

    private boolean isListShown;
    private View listView;
    private ProgressBar progressBar;
    private FloatingActionButton btnAnalyzeFromFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, null);
        listView = view.findViewById(R.id.list_view_list);
        progressBar = (ProgressBar) view.findViewById(R.id.list_view_progress_bar);
        btnAnalyzeFromFile = (FloatingActionButton) view.findViewById(R.id.btn_analyze_not_installed);
        btnAnalyzeFromFile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState == null) {
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_analyze_not_installed:
                // show file picker
                startFilePicker(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_analyze_not_installed:
                startFilePicker(true);
        }
    }

    /**
     * Currently it is called only after APK file is selected from storage
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle picked apk file and
        if (requestCode == ApkFilePicker.REQUEST_PICK_APK && resultCode == RESULT_OK) {
            AnalyzeFragment parentFragment = (AnalyzeFragment) getParentFragment();
            parentFragment.itemClicked(null, ApkFilePicker.getPathOnActivityResult(data, getContext()));
        }
    }

    /**
     *  This is called only when storage permission is granted as we use permission granting only when
     *  reading APK file
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUESTCODE_STORAGE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFilePicker(false);
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Start file picker activity in order to select APK file from storage.
     * @param withPermissionCheck if true, permissions are requested
     */
    private void startFilePicker(boolean withPermissionCheck) {
        if (withPermissionCheck) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE_PERMISSIONS);
            } else {
                startFilePicker(false);
            }
        } else {
            startActivityForResult(ApkFilePicker.getFilePickerIntent(), ApkFilePicker.REQUEST_PICK_APK);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String currentFilter = !TextUtils.isEmpty(newText) ? newText : null;
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
        parentFragment.itemClicked(appBasicData.getPackageName(), null);
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

    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    public void setListShown(boolean shown, boolean animate) {
        if (isListShown == shown) {
            return;
        }
        isListShown = shown;
        if (shown) {
            if (animate) {
                progressBar.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                listView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            }
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                progressBar.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                listView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            }
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }
}
