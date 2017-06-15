package sk.styk.martin.apkanalyzer.activity;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.business.InstalledAppsRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends Fragment {

    private RecyclerView mRecycleView;
    private SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;
    private InstalledAppsRepository installedAppsRepository;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        installedAppsRepository = new InstalledAppsRepository(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(sk.styk.martin.apkanalyzer.R.layout.fragment_item_list, container, false);
        mRecycleView = (RecyclerView) view.findViewById(sk.styk.martin.apkanalyzer.R.id.item_list);
        mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(getActivity(), installedAppsRepository.getAll());
        mRecycleView.setAdapter(mSimpleItemRecyclerViewAdapter);

        return view;
    }

}
