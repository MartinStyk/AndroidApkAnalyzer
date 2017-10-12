package sk.styk.martin.apkanalyzer.adapter.pager;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Activity;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Certificate;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Feature;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_File;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_General;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Permission;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Provider;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Receiver;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Resource;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Service;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class AppDetailPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private AppDetailData data;

    public AppDetailPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                args.putParcelable(AppDetailFragment.ARG_CHILD, data.getGeneralData());
                fragment = new AppDetailFragment_General();
                break;

            case 1:
                args.putParcelable(AppDetailFragment.ARG_CHILD, data.getCertificateData());
                fragment = new AppDetailFragment_Certificate();
                break;

            case 2:
                args.putParcelable(AppDetailFragment.ARG_CHILD, data.getResourceData());
                fragment = new AppDetailFragment_Resource();
                break;

            case 3:
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, (ArrayList<? extends Parcelable>) data.getActivityData());
                fragment = new AppDetailFragment_Activity();
                break;

            case 4:
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, (ArrayList<? extends Parcelable>) data.getServiceData());
                fragment = new AppDetailFragment_Service();
                break;

            case 5:
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, (ArrayList<? extends Parcelable>) data.getContentProviderData());
                fragment = new AppDetailFragment_Provider();
                break;

            case 6:
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, (ArrayList<? extends Parcelable>) data.getBroadcastReceiverData());
                fragment = new AppDetailFragment_Receiver();
                break;

            case 7:
                args.putStringArrayList(AppDetailFragment.ARG_CHILD, (ArrayList<String>) data.getPermissionData().getUsesPermissions());
                fragment = new AppDetailFragment_Permission();
                break;

            case 8:
                args.putStringArrayList(AppDetailFragment.ARG_CHILD,(ArrayList<String>) data.getPermissionData().getDefinesPermissions());
                fragment = new AppDetailFragment_Permission();
                break;

            case 9:
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, (ArrayList<? extends Parcelable>) data.getFeatureData());
                fragment = new AppDetailFragment_Feature();
                break;

            case 10:
                args.putParcelable(AppDetailFragment.ARG_CHILD, data.getFileData());
                fragment = new AppDetailFragment_File();
                break;

            default:
                return null;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.general);
            case 1:
                return context.getResources().getString(R.string.certificate);
            case 2:
                return context.getResources().getString(R.string.resources);
            case 3:
                return context.getResources().getString(R.string.activities);
            case 4:
                return context.getResources().getString(R.string.services);
            case 5:
                return context.getResources().getString(R.string.content_providers);
            case 6:
                return context.getResources().getString(R.string.broadcast_receivers);
            case 7:
                return context.getResources().getString(R.string.permissions);
            case 8:
                return context.getResources().getString(R.string.defined_permissions);
            case 9:
                return context.getResources().getString(R.string.features);
            case 10:
                return context.getResources().getString(R.string.files);
        }
        return "TODO";
    }

    public void dataChange(AppDetailData data) {
        this.data = data;
    }

}