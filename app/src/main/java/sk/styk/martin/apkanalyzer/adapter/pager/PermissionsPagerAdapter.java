package sk.styk.martin.apkanalyzer.adapter.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailPagerFragment;
import sk.styk.martin.apkanalyzer.activity.permission.PermissionsAppListFragment;
import sk.styk.martin.apkanalyzer.activity.permission.PermissionsGeneralDetailsFragment;
import sk.styk.martin.apkanalyzer.model.detail.PermissionData;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class PermissionsPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<String> grantedPackages = new ArrayList<>(0);
    private List<String> notGrantedPackages = new ArrayList<>(0);
    private PermissionData permissionData;

    public PermissionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                args.putParcelable(PermissionDetailPagerFragment.ARG_CHILD, permissionData);
                args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_GRANTED_APPS, grantedPackages.size());
                args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_NOT_GRANTED_APPS, notGrantedPackages.size());
                fragment = new PermissionsGeneralDetailsFragment();
                break;

            case 1:
                args.putStringArrayList(PermissionDetailPagerFragment.ARG_CHILD, (ArrayList<String>) grantedPackages);
                fragment = new PermissionsAppListFragment();
                break;

            case 2:
                args.putStringArrayList(PermissionDetailPagerFragment.ARG_CHILD, (ArrayList<String>) notGrantedPackages);
                fragment = new PermissionsAppListFragment();
                break;

            default:
                return null;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.permissions_detail);
            case 1:
                return context.getResources().getString(R.string.permissions_granted);
            case 2:
                return context.getResources().getString(R.string.permissions_not_granted);
        }
        return "TODO";
    }

    public void dataChange(LocalPermissionData data) {
        this.permissionData = data.getPermissionData();
        this.grantedPackages = data.getGrantedPackageNames();
        this.notGrantedPackages = data.getNotGrantedPackageNames();
    }
}