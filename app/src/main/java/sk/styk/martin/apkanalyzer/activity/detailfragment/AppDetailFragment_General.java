package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.GeneralData;
import sk.styk.martin.apkanalyzer.view.DetailItemView;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class AppDetailFragment_General extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_general, container, false);

        GeneralData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);

        ((DetailItemView) rootView.findViewById(R.id.item_application_name)).setValue(data.getApplicationName());
        ((DetailItemView) rootView.findViewById(R.id.item_package_name)).setValue(data.getPackageName());
        ((DetailItemView) rootView.findViewById(R.id.item_process_name)).setValue(data.getProcessName());
        ((DetailItemView) rootView.findViewById(R.id.item_version_name)).setValue(data.getVersionName());
        ((DetailItemView) rootView.findViewById(R.id.item_version_code)).setValue(String.valueOf(data.getVersionCode()));
        ((DetailItemView) rootView.findViewById(R.id.item_system_application)).setValue(data.isSystemApp() ? getString(R.string.yes) : getString(R.string.no));
        ((DetailItemView) rootView.findViewById(R.id.item_application_description)).setValue(data.getDescription());
        ((DetailItemView) rootView.findViewById(R.id.item_target_sdk)).setValue(String.valueOf(data.getTargetSdkVersion()));
        ((DetailItemView) rootView.findViewById(R.id.item_target_android_version)).setValue(data.getTargetSdkLabel());
        ((DetailItemView) rootView.findViewById(R.id.item_min_sdk)).setValue(String.valueOf(data.getMinSdkVersion()));
        ((DetailItemView) rootView.findViewById(R.id.item_min_android_version)).setValue(data.getMinSdkLabel());
        ((DetailItemView) rootView.findViewById(R.id.item_apk_directory)).setValue(data.getApkDirectory());
        ((DetailItemView) rootView.findViewById(R.id.item_data_directory)).setValue(data.getDataDirectory());
        ((DetailItemView) rootView.findViewById(R.id.item_install_location)).setValue(data.getInstallLocation());
        ((DetailItemView) rootView.findViewById(R.id.item_apk_size)).setValue(String.valueOf(Formatter.formatShortFileSize(getActivity(), data.getApkSize())));

        String installTime = DateUtils.formatDateTime(getActivity(), data.getFirstInstallTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_first_install_time)).setValue(installTime);

        String updateTime = DateUtils.formatDateTime(getActivity(), data.getLastUpdateTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_last_update_time)).setValue(updateTime);

        return rootView;
    }
}
