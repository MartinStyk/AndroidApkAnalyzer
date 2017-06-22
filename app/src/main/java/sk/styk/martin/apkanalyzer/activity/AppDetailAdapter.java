package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class AppDetailAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(getCount());

    private AppBasicInfo data;

    public AppDetailAdapter(FragmentManager fm) {
        super(fm);
        for (int i = 0; i < getCount(); i++) {
            Fragment fragment = new AppDetailFragment_Basic();
            Bundle args = new Bundle();
            args.putInt(AppDetailFragment_Basic.ARG, i + 1);
            fragment.setArguments(args);
            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        Bundle args = new Bundle();
        args.putParcelable(AppDetailFragment_Basic.ARG, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "FRAGMENT " + (position + 1);
    }

    public void dataChange(AppBasicInfo data){
        this.data = data;
    }

}