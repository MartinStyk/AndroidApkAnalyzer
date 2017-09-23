package sk.styk.martin.apkanalyzer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.R;

/**
 * Parent fragment for AppListFragment and AppDetailFragment.
 * Purpose of thi fragment is to provide separated screen areas in landscape orientation of w900dp devices
 */
public class AnalyzeFragment extends Fragment {

    private View rootView;

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_analyze, container, false);

        Fragment fragment = new AppListFragment();
        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.app_list_container, fragment).commit();

        return rootView;
    }

    private boolean isTwoPane() {
        return (rootView != null && rootView.findViewById(R.id.app_detail_container) != null);
    }

    public void itemClicked(String packageName) {
        if (!isTwoPane()) {
            Context context = getContext();
            Intent intent = new Intent(context, AppDetailActivity.class);
            intent.putExtra(AppDetailFragment.ARG_PACKAGE_NAME, packageName);
            context.startActivity(intent);
        } else {
            Bundle arguments = new Bundle();
            arguments.putString(AppDetailFragment.ARG_PACKAGE_NAME, packageName);
            AppDetailFragment fragment = new AppDetailFragment();
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction().replace(R.id.app_detail_container, fragment).commit();
        }
    }
}
