package sk.styk.martin.apkanalyzer.activity.permission;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.dialog.SimpleTextDialog;
import sk.styk.martin.apkanalyzer.adapter.pager.PermissionsPagerAdapter;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData;

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
public class PermissionDetailPagerFragment extends Fragment {

    public static final String TAG = PermissionDetailPagerFragment.class.getSimpleName();

    public static final String ARG_PERMISSIONS_DATA = "permission_args";
    public static final String ARG_CHILD = "permission_args_to_my_sweetest_child";

    private PermissionsPagerAdapter adapter;

    // we add description button to toolbar in this fragment
    private MenuItem description;
    private String permissionDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        adapter = new PermissionsPagerAdapter(getActivity(), getFragmentManager());
        LocalPermissionData permissionData = getArguments().getParcelable(ARG_PERMISSIONS_DATA);

        if (permissionData != null)
            loadPermissionDescription(getContext().getPackageManager(), permissionData.getPermissionData().getName());

        adapter.dataChange(permissionData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_permission_detail_pager, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        description = menu.add(R.string.description);
        description.setIcon(R.drawable.ic_info_white);
        description.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == description.getItemId()) {
            SimpleTextDialog.newInstance(getString(R.string.description), permissionDescription).show(getFragmentManager(), SimpleTextDialog.class.getSimpleName());
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPermissionDescription(final PackageManager packageManager, final String permissionName) {
        new Thread() {
            @Override
            public void run() {
                CharSequence desc = null;
                try {
                    desc = packageManager.getPermissionInfo(permissionName, PackageManager.GET_META_DATA).loadDescription(packageManager);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                permissionDescription = desc == null ? getContext().getString(R.string.NA) : desc.toString();
            }
        }.start();
    }

}
