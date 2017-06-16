package sk.styk.martin.apkanalyzer.activity;


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
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends Fragment implements ItemListLoadTask.Callback, View.OnClickListener {

    private SimpleItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private View listContainerView;
    private ProgressBar loadingBar;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

        // do not load data on configuration change
        if (savedInstanceState == null) {
            adapter = new SimpleItemRecyclerViewAdapter(getActivity(), new ArrayList<AppBasicInfo>());
            new ItemListLoadTask(getActivity(), this).execute();
        }
        // we need to set context for fragment manager
        adapter.setContext(getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Callback from async task
     */
    @Override
    public void onTaskCompleted(List<AppBasicInfo> applicationInfoList) {
        adapter.dataChange(applicationInfoList);
        loadingBar.setVisibility(View.GONE);
        listContainerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskStart() {
        loadingBar.setVisibility(View.VISIBLE);
        listContainerView.setVisibility(View.GONE);
    }

    /**
     * Handle click events from radio button
     */
    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radio_all_apps:
                if (checked)
                    Toast.makeText(getActivity(), "All", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_system_apps:
                if (checked)
                    Toast.makeText(getActivity(), "System", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_user_apps:
                if (checked)
                    Toast.makeText(getActivity(), "user", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
