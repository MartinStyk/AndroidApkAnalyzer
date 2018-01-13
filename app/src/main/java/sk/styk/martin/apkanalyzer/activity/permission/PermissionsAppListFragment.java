package sk.styk.martin.apkanalyzer.activity.permission;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.adapter.AppListRecyclerAdapter;
import sk.styk.martin.apkanalyzer.business.task.AppListFromPackageNamesLoader;
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionAppListBinding;
import sk.styk.martin.apkanalyzer.model.list.AppListData;
import sk.styk.martin.apkanalyzer.model.permissions.PermissionStatus;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class PermissionsAppListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<AppListData>> {

    private FragmentPermissionAppListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(AppListFromPackageNamesLoader.ID, getArguments(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPermissionAppListBinding.inflate(getLayoutInflater());

        binding.recyclerViewAppList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return binding.getRoot();
    }

    @Override
    public Loader<List<AppListData>> onCreateLoader(int id, Bundle args) {
        return new AppListFromPackageNamesLoader(getContext(),  args.getStringArrayList(PermissionDetailFragment.ARG_CHILD));
    }

    @Override
    public void onLoadFinished(Loader<List<AppListData>> loader, List<AppListData> data) {
        binding.recyclerViewAppList.setAdapter(new AppListRecyclerAdapter(data));
        binding.listViewProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<AppListData>> loader) {
    }
}
