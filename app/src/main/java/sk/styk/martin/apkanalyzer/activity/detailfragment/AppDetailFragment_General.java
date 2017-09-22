package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.business.task.FileCopyService;
import sk.styk.martin.apkanalyzer.model.detail.GeneralData;
import sk.styk.martin.apkanalyzer.view.DetailItemView;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class AppDetailFragment_General extends Fragment implements View.OnClickListener {

    private static final int REQUEST_STORAGE_PERMISSION = 11;

    private GeneralData data;
    private Button copyBtn;
    private Button manifestBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_app_detail_general, container, false);

        data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);

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
        ((DetailItemView) rootView.findViewById(R.id.item_apk_size)).setValue(Formatter.formatShortFileSize(getActivity(), data.getApkSize()));

        String installTime = DateUtils.formatDateTime(getActivity(), data.getFirstInstallTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_first_install_time)).setValue(installTime);

        String updateTime = DateUtils.formatDateTime(getActivity(), data.getLastUpdateTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_last_update_time)).setValue(updateTime);

        copyBtn = (Button) rootView.findViewById(R.id.btn_copy);
        copyBtn.setOnClickListener(this);

        manifestBtn = (Button) rootView.findViewById(R.id.btn_show_manifest);
        manifestBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == copyBtn.getId()) {

            //request permission and handle result
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            } else {
                exportApkFile();
            }
        } else if (v.getId() == manifestBtn.getId()) {
            Intent intent = new Intent(getActivity(), ManifestActivity.class);
            intent.putExtra(ManifestActivity.PACKAGE_NAME_FOR_MANIFEST_REQUEST, data.getPackageName());
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportApkFile();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void exportApkFile() {
        File source = new File(data.getApkDirectory());
        File target = new File(Environment.getExternalStorageDirectory(), data.getPackageName() + ".apk");

        Intent intent = new Intent(getActivity(), FileCopyService.class);
        intent.putExtra(FileCopyService.SOURCE_FILE, source.getAbsolutePath());
        intent.putExtra(FileCopyService.TARGET_FILE, target.getAbsolutePath());

        getActivity().startService(intent);

        Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.copy_apk_background, target.getAbsolutePath()), Snackbar.LENGTH_SHORT).show();
    }

}
