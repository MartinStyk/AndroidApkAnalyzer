package sk.styk.martin.apkanalyzer.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.detailfragment.ManifestActivity;
import sk.styk.martin.apkanalyzer.adapter.pager.AppDetailPagerAdapter;
import sk.styk.martin.apkanalyzer.business.task.AppDetailLoader;
import sk.styk.martin.apkanalyzer.business.task.FileCopyService;
import sk.styk.martin.apkanalyzer.business.task.upload.AppDataUploadTask;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AppListActivity}
 * in two-pane mode (on tablets) or a {@link AppDetailActivity}
 * on handsets.
 */
public class AppDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<AppDetailData>, View.OnClickListener {

    public static final String TAG = AppDetailFragment.class.getSimpleName();
    public static final String ARG_PACKAGE_NAME = "packageName";
    public static final String ARG_PACKAGE_PATH = "packagePath";
    public static final String ARG_CHILD = "dataForChild";
    private static final int REQUEST_STORAGE_PERMISSION = 11;
    private AppDetailData data;

    private CollapsingToolbarLayout appBarLayout;
    private ImageView appBarLayuotImageView;

    private AppDetailPagerAdapter adapter;
    private ProgressBar loadingBar;
    private ViewPager viewPager;
    private TextView errorLoadingText;

    private boolean isDialogShowing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AppDetailPagerAdapter(getActivity(), getFragmentManager());
        getLoaderManager().initLoader(AppDetailLoader.ID, getArguments(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(sk.styk.martin.apkanalyzer.R.layout.fragment_app_detail, container, false);

        loadingBar = rootView.findViewById(R.id.item_detail_loading);
        appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        appBarLayuotImageView = getActivity().findViewById(R.id.toolbar_layout_image);
        errorLoadingText = rootView.findViewById(R.id.item_detail_error);

        viewPager = rootView.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // if we are in 2-pane mode initialize floating button
        FloatingActionButton actionButton = rootView.findViewById(R.id.btn_actions);
        if (actionButton != null) {
            actionButton.setOnClickListener(this);
        }

        if (data != null) {
            onLoadFinished(null, data);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        if (isDialogShowing) {
            showActionDialog();
        }
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDialogShowing", isDialogShowing);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            isDialogShowing = savedInstanceState.getBoolean("isDialogShowing");
    }

    @Override
    public Loader<AppDetailData> onCreateLoader(int id, Bundle args) {
        return new AppDetailLoader(getActivity(), args.getString(ARG_PACKAGE_NAME), args.getString(ARG_PACKAGE_PATH));
    }

    @Override
    public void onLoadFinished(Loader<AppDetailData> loader, AppDetailData data) {
        this.data = data;
        loadingBar.setVisibility(View.GONE);

        if (data == null) {
            errorLoadingText.setVisibility(View.VISIBLE);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getString(R.string.loading_failed));
            }
        } else {
            if (appBarLayout != null) {
                appBarLayout.setTitle(data.getGeneralData().getPackageName());
                appBarLayuotImageView.setImageDrawable(data.getGeneralData().getIcon());
            }
            viewPager.setVisibility(View.VISIBLE);

            adapter.dataChange(data);

            new AppDataUploadTask(getContext()).execute(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<AppDetailData> loader) {
        this.data = null;
    }

    @Override
    public void onClick(View view) {
        // show actions only when data is loaded
        if (data != null)
            showActionDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportApkFile();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void showActionDialog() {
        // if data load failed or data is not loaded so far, do not show action dialogs
        if (data == null)
            return;

        LayoutInflater factory = LayoutInflater.from(getContext());
        final View dialogView = factory.inflate(R.layout.dialog_apk_actions, null);

        // setup dialog
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.pick_action))
                .setView(dialogView)
                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isDialogShowing = false;
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        isDialogShowing = false;
                    }
                }).create();


        // setup buttons
        dialogView.findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    exportApkFile();
                }
            }
        });

        dialogView.findViewById(R.id.btn_share_apk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                Uri uri = Uri.fromFile(new File(data.getGeneralData().getApkDirectory()));

                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("application/vnd.android.package-archive");

                dialog.dismiss();

                try {
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_apk_using)));
                } catch (ActivityNotFoundException e) {
                    // this might happen on Android 4.4
                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.activity_not_found_sharing, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        // allow manifest and built-in app info only for installed packages
        if (data.isAnalyzedInstalledPackage()) {
            dialogView.findViewById(R.id.btn_show_manifest).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start manifest activity
                    Intent intent = new Intent(getActivity(), ManifestActivity.class);
                    intent.putExtra(ManifestActivity.PACKAGE_NAME_FOR_MANIFEST_REQUEST, data.getGeneralData().getPackageName());

                    dialog.dismiss();
                    startActivity(intent);
                }
            });

            dialogView.findViewById(R.id.btn_show_app_system_page).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent systemInfoIntent = new Intent();
                    systemInfoIntent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    systemInfoIntent.setData(Uri.parse("package:" + data.getGeneralData().getPackageName()));

                    dialog.dismiss();
                    startActivity(systemInfoIntent);
                }
            });
        } else if (data.isAnalyzedApkFile()) {
            dialogView.findViewById(R.id.btn_show_manifest).setVisibility(View.GONE);
            dialogView.findViewById(R.id.btn_show_app_system_page).setVisibility(View.GONE);
        }

        isDialogShowing = true;
        dialog.show();
    }

    private void exportApkFile() {
        String targetFile = FileCopyService.startService(getActivity(), data);
        Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.copy_apk_background, targetFile), Snackbar.LENGTH_LONG).show();
    }
}
