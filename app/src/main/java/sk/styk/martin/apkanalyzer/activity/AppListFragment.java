package sk.styk.martin.apkanalyzer.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
public class AppListFragment extends Fragment implements ItemListLoadTask.Callback, AppListRecyclerViewAdapter.Callback, View.OnClickListener {

    private AppListRecyclerViewAdapter adapter;
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
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);

        // Set the onClick for each of our views as the one implemented by this Fragment
        view.findViewById(R.id.radio_all_apps).setOnClickListener(this);
        view.findViewById(R.id.radio_system_apps).setOnClickListener(this);
        view.findViewById(R.id.radio_user_apps).setOnClickListener(this);

        listContainerView = view.findViewById(R.id.item_list_container);
        loadingBar = (ProgressBar) view.findViewById(R.id.item_list_loading);
        recyclerView = (RecyclerView) view.findViewById(R.id.item_list);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));

        // do not load data on configuration change
        if (savedInstanceState == null) {
            adapter = new AppListRecyclerViewAdapter(getActivity(), new ArrayList<AppBasicInfo>(), this);
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

    // TODO remove positien and use only appBasicInfo
    @Override
    public void onItemClick(View view, AppBasicInfo appBasicInfo, int position) {
        if (!MainActivity.mTwoPane) {
            Context context = view.getContext();
            Intent intent = new Intent(context, AppDetailActivity.class);
            intent.putExtra(AppDetailFragment.ARG_ITEM_ID, position);
            context.startActivity(intent);
        } else {
            // show details fragment
            Bundle arguments = new Bundle();
            arguments.putInt(AppDetailFragment.ARG_ITEM_ID, position);
            AppDetailFragment fragment = new AppDetailFragment();
            fragment.setArguments(arguments);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
        }
    }
}
