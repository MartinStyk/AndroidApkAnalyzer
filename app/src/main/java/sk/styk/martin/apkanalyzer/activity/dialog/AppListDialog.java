package sk.styk.martin.apkanalyzer.activity.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.adapter.AppListRecyclerAdapter;
import sk.styk.martin.apkanalyzer.business.task.AppListFromPackageNamesLoader;
import sk.styk.martin.apkanalyzer.databinding.DialogAppListBinding;
import sk.styk.martin.apkanalyzer.model.list.AppListData;

/**
 * Created by Martin Styk on 05.01.2018.
 */
public class AppListDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<List<AppListData>> {

    public static final String PACKAGES = "packages";
    private DialogAppListBinding binding;

    public static AppListDialog newInstance(ArrayList<String> packageNames) {
        AppListDialog frag = new AppListDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(PACKAGES, packageNames);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(AppListFromPackageNamesLoader.ID, getArguments(), this);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DialogAppListBinding.inflate(getActivity().getLayoutInflater());

        binding.recyclerViewApplications.setAdapter(new AppListRecyclerAdapter(new ArrayList<AppListData>(0)));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerViewApplications.getContext(), DividerItemDecoration.VERTICAL);
        binding.recyclerViewApplications.addItemDecoration(dividerItemDecoration);


        return new AlertDialog.Builder(getActivity())
                .setView(binding.getRoot())
                .setTitle(R.string.apps)
                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
    }

    @Override
    public Loader<List<AppListData>> onCreateLoader(int id, Bundle args) {
        return new AppListFromPackageNamesLoader(getContext(), args.getStringArrayList(PACKAGES));
    }

    @Override
    public void onLoadFinished(Loader<List<AppListData>> loader, List<AppListData> data) {
        binding.recyclerViewApplications.setAdapter(new AppListRecyclerAdapter(data));
    }

    @Override
    public void onLoaderReset(Loader<List<AppListData>> loader) {
    }
}