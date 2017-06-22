package sk.styk.martin.apkanalyzer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Basic;
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Certificate;
import sk.styk.martin.apkanalyzer.model.AppDetailData;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class AppDetailAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments = new Fragment[getCount()];
    private Context context;
    private AppDetailData data;

    public AppDetailAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        fragments[0] = new AppDetailFragment_Basic();
        fragments[1] = new AppDetailFragment_Certificate();
        fragments[2] = new AppDetailFragment_Basic();
        fragments[3] = new AppDetailFragment_Basic();
        fragments[4] = new AppDetailFragment_Basic();
        fragments[5] = new AppDetailFragment_Basic();
        fragments[6] = new AppDetailFragment_Basic();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments[position];
        Bundle args = new Bundle();
        args.putParcelable(AppDetailFragment.ARG_CHILD, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return context.getResources().getString(R.string.certificate);
        }
        return "FRAGMENT " + (position + 1);
    }

    public void dataChange(AppDetailData data) {
        this.data = data;
    }

}