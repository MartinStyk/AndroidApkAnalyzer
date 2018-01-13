package sk.styk.martin.apkanalyzer.activity.permission;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding;
import sk.styk.martin.apkanalyzer.model.detail.PermissionData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class PermissionsGeneralDetailsFragment extends Fragment {

    private FragmentPermissionDetailGeneralBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPermissionDetailGeneralBinding.inflate(getLayoutInflater());

        PermissionData permissionData = getArguments().getParcelable(PermissionDetailPagerFragment.ARG_CHILD);
        binding.setPermission(permissionData);

        return binding.getRoot();
    }
}
