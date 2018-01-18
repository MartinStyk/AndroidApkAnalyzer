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
import sk.styk.martin.apkanalyzer.model.detail.GeneralData;
import sk.styk.martin.apkanalyzer.util.InstallLocationHelper;
import sk.styk.martin.apkanalyzer.view.DetailItemView;

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */

public class AppDetailFragment_General extends Fragment {

    private GeneralData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_app_detail_general, container, false);

        data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);

        ((DetailItemView) rootView.findViewById(R.id.item_application_name)).setValueText(data.getApplicationName());
        ((DetailItemView) rootView.findViewById(R.id.item_package_name)).setValueText(data.getPackageName());
        ((DetailItemView) rootView.findViewById(R.id.item_process_name)).setValueText(data.getProcessName());
        ((DetailItemView) rootView.findViewById(R.id.item_version_name)).setValueText(data.getVersionName());
        ((DetailItemView) rootView.findViewById(R.id.item_version_code)).setValueText(String.valueOf(data.getVersionCode()));
        ((DetailItemView) rootView.findViewById(R.id.item_system_application)).setValueText(data.isSystemApp() ? getString(R.string.yes) : getString(R.string.no));
        ((DetailItemView) rootView.findViewById(R.id.item_uid)).setValueText(String.valueOf(data.getUid()));
        ((DetailItemView) rootView.findViewById(R.id.item_application_description)).setValueText(data.getDescription());
        ((DetailItemView) rootView.findViewById(R.id.item_application_app_source)).setValueText(data.getSource() == null ? null : data.getSource().toString());
        ((DetailItemView) rootView.findViewById(R.id.item_target_sdk)).setValueText(String.valueOf(data.getTargetSdkVersion()));
        ((DetailItemView) rootView.findViewById(R.id.item_target_android_version)).setValueText(data.getTargetSdkLabel());
        ((DetailItemView) rootView.findViewById(R.id.item_min_sdk)).setValueText(String.valueOf(data.getMinSdkVersion()));
        ((DetailItemView) rootView.findViewById(R.id.item_min_android_version)).setValueText(data.getMinSdkLabel());
        ((DetailItemView) rootView.findViewById(R.id.item_apk_directory)).setValueText(data.getApkDirectory());
        ((DetailItemView) rootView.findViewById(R.id.item_data_directory)).setValueText(data.getDataDirectory());
        ((DetailItemView) rootView.findViewById(R.id.item_install_location)).setValueText(InstallLocationHelper.INSTANCE.showLocalizedLocation(data.getInstallLocation(), getContext()));
        ((DetailItemView) rootView.findViewById(R.id.item_apk_size)).setValueText(Formatter.formatShortFileSize(getActivity(), data.getApkSize()));

        String installTime = DateUtils.formatDateTime(getActivity(), data.getFirstInstallTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_first_install_time)).setValueText(installTime);

        String updateTime = DateUtils.formatDateTime(getActivity(), data.getLastUpdateTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_last_update_time)).setValueText(updateTime);

        return rootView;
    }
}
