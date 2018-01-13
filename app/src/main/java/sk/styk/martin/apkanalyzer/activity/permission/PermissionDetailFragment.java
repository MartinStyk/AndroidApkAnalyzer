package sk.styk.martin.apkanalyzer.activity.permission;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.adapter.pager.PermissionsPagerAdapter;
import sk.styk.martin.apkanalyzer.model.detail.PermissionData;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData;
import sk.styk.martin.apkanalyzer.model.permissions.PermissionStatus;

public class PermissionDetailFragment extends Fragment {

    public static final String TAG = PermissionDetailFragment.class.getSimpleName();

    public static final String ARG_PERMISSIONS_DATA = "permission_args";
    public static final String ARG_CHILD = "permission_args_to_my_sweetest_child";

    private LocalPermissionData permissionData;

    private CollapsingToolbarLayout appBarLayout;
    private TextView toolbarDescription;

    private PermissionsPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new PermissionsPagerAdapter(getActivity(), getFragmentManager());
        permissionData = getArguments().getParcelable(ARG_PERMISSIONS_DATA);

        adapter.dataChange(permissionData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_permission_detail, container, false);

        appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        toolbarDescription = getActivity().findViewById(R.id.toolbar_layout_description);

        appBarLayout.setTitle(permissionData.getPermissionData().getName());
        toolbarDescription.setText(permissionData.getPermissionData().getSimpleName());

        viewPager = rootView.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
}
