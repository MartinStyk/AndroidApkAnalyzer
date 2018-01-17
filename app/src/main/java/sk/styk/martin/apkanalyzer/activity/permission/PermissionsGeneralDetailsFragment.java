package sk.styk.martin.apkanalyzer.activity.permission;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding;
import sk.styk.martin.apkanalyzer.model.detail.PermissionData;

/**
 * Created by Martin Styk on 13.01.2018.
 */
public class PermissionsGeneralDetailsFragment extends Fragment {

    private static final String TAG = PermissionsGeneralDetailsFragment.class.getSimpleName();

    public static final String ARG_NUMBER_GRANTED_APPS = "apps_granted_perm";
    public static final String ARG_NUMBER_NOT_GRANTED_APPS = "apps_not_granted_perm";

    private FragmentPermissionDetailGeneralBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPermissionDetailGeneralBinding.inflate(getLayoutInflater());

        PermissionData permissionData = getArguments().getParcelable(PermissionDetailPagerFragment.ARG_CHILD);
        binding.setPermission(permissionData);
        binding.setContext(getContext());
        binding.setGrantedApps(getArguments().getInt(ARG_NUMBER_GRANTED_APPS));
        binding.setNotGrantedApps(getArguments().getInt(ARG_NUMBER_NOT_GRANTED_APPS));

        return binding.getRoot();
    }

}
