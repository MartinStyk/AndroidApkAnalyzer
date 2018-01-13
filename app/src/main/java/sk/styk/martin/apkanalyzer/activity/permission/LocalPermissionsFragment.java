package sk.styk.martin.apkanalyzer.activity.permission;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.adapter.PermissionListAdapter;
import sk.styk.martin.apkanalyzer.business.task.LocalPermissionsLoader;
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalPermissionsBinding;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData;

public class LocalPermissionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<LocalPermissionData>>, LocalPermissionsLoader.ProgressCallback {

    private FragmentLocalPermissionsBinding binding;
    private List<LocalPermissionData> data;

    private PermissionListAdapter permissionListAdapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocalPermissionsBinding.inflate(inflater);

        binding.recyclerViewPermissions.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // We need to re-set callback of loader in case of configuration change
        LocalPermissionsLoader loader = (LocalPermissionsLoader) getLoaderManager().initLoader(LocalPermissionsLoader.ID, null, this);
        if (loader != null) {
            loader.setCallbackReference(this);
        }

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Hide action bar item for searching
        menu.clear();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<List<LocalPermissionData>> onCreateLoader(int id, Bundle args) {
        return new LocalPermissionsLoader(getContext(), this);
    }

    @Override
    public void onLoadFinished(Loader<List<LocalPermissionData>> loader, List<LocalPermissionData> data) {
        this.data = data;

        permissionListAdapter = new PermissionListAdapter(data);
        binding.recyclerViewPermissions.swapAdapter(permissionListAdapter, false);

        binding.content.setVisibility(View.VISIBLE);
        binding.contentLoading.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<LocalPermissionData>> loader) {
        this.data = null;
    }

    @Override
    public void onProgressChanged(int currentProgress, int maxProgress) {
        binding.contentLoadingBar.setMax(maxProgress);
        binding.contentLoadingBar.setProgress(currentProgress);
    }

}
