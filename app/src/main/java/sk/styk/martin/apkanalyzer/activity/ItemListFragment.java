package sk.styk.martin.apkanalyzer.activity;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.ItemListLoadTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends Fragment implements ItemListLoadTask.OnTaskCompleted, View.OnClickListener {

    private SimpleItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private View listContainerView;
    private ProgressBar loadingBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the onClick for each of our views as the one implemented by this Fragment
        view.findViewById(R.id.radio_all_apps).setOnClickListener(this);
        view.findViewById(R.id.radio_system_apps).setOnClickListener(this);
        view.findViewById(R.id.radio_user_apps).setOnClickListener(this);

        listContainerView = view.findViewById(R.id.item_list_container);
        recyclerView = (RecyclerView) view.findViewById(R.id.item_list);
        loadingBar = (ProgressBar) view.findViewById(R.id.item_list_loading);
        adapter = new SimpleItemRecyclerViewAdapter(getActivity(), new ArrayList<ApplicationInfo>());
        recyclerView.setAdapter(adapter);

        new ItemListLoadTask(getActivity(), this).execute();
        return view;
    }

    /**
     * Callback from async task
     */
    @Override
    public void onTaskCompleted(List<ApplicationInfo> applicationInfoList) {
        adapter.dataChange(applicationInfoList);
        loadingBar.setVisibility(View.GONE);
        listContainerView.setVisibility(View.VISIBLE);
    }

    /**
     * Handle click events from radio button
     */
    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {

            case R.id.radio_all_apps:
                Toast.makeText(getActivity(), "All", Toast.LENGTH_LONG).show();

            case R.id.radio_system_apps:
                Toast.makeText(getActivity(), "System", Toast.LENGTH_LONG).show();

            case R.id.radio_user_apps:
                Toast.makeText(getActivity(), "user", Toast.LENGTH_LONG).show();
        }
    }
}
