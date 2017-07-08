package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.adapter.PermissionListAdapter;
import sk.styk.martin.apkanalyzer.model.PermissionData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class AppDetailFragment_Permission extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_detail_permission, container, false);

        PermissionData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_permission);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.Adapter adapter = new PermissionListAdapter(data.getUsesPermissions());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return rootView;
    }
}
