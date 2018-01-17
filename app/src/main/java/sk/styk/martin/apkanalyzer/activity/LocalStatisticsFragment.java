package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.dialog.AppListDialog;
import sk.styk.martin.apkanalyzer.business.task.LocalStatisticsLoader;
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalStatisticsBinding;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData;
import sk.styk.martin.apkanalyzer.util.AndroidVersionHelper;
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter;

/**
 * @author Martin Styk
 */
public class LocalStatisticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<LocalStatisticsData>, LocalStatisticsLoader.ProgressCallback {

    FragmentLocalStatisticsBinding binding;
    LocalStatisticsData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocalStatisticsBinding.inflate(inflater);

        // We need to re-set callback of loader in case of configuration change
        LocalStatisticsLoader loader = (LocalStatisticsLoader) getLoaderManager().initLoader(LocalStatisticsLoader.ID, null, this);
        if (loader != null) {
            loader.setCallbackReference(this);
        }

        setHasOptionsMenu(true);

        setupChart(binding.chartMinSdk);
        setupChart(binding.chartTargetSdk);
        setupChart(binding.chartInstallLocation);
        setupChart(binding.chartSignAlgorithm);
        setupChart(binding.chartAppSource);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Hide action bar item for searching
        menu.clear();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<LocalStatisticsData> onCreateLoader(int id, Bundle args) {
        return new LocalStatisticsLoader(getContext(), this);
    }

    @Override
    public void onLoadFinished(Loader<LocalStatisticsData> loader, LocalStatisticsData data) {
        this.data = data;

        binding.itemAnalyzeSuccess.setValueText(data.getAnalyzeSuccess().getCount().toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.getAnalyzeSuccess().getPercentage()) + "%)");
        binding.itemAnalyzeFailed.setValueText(data.getAnalyzeFailed().getCount().toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.getAnalyzeFailed().getPercentage()) + "%)");
        binding.itemSystemApps.setValueText(data.getSystemApps().getCount().toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.getSystemApps().getPercentage()) + "%)");

        binding.chartMinSdk.setColumnChartData(getSdkColumnChart(data.getMinSdk(), getResources().getColor(R.color.primary)));
        binding.chartTargetSdk.setColumnChartData(getSdkColumnChart(data.getTargetSdk(), getResources().getColor(R.color.primary)));
        binding.chartInstallLocation.setColumnChartData(getColumnChart(data.getInstallLocation(), R.string.install_loc, getResources().getColor(R.color.primary)));
        binding.chartSignAlgorithm.setColumnChartData(getColumnChart(data.getSignAlgorithm(), R.string.sign_algorithm, getResources().getColor(R.color.primary)));
        binding.chartAppSource.setColumnChartData(getColumnChart(data.getAppSource(), R.string.app_source, getResources().getColor(R.color.primary)));

        binding.chartMinSdk.setOnValueTouchListener(new SdkValueTouchListener(binding.chartMinSdk, data.getMinSdk()));
        binding.chartTargetSdk.setOnValueTouchListener(new SdkValueTouchListener(binding.chartTargetSdk, data.getTargetSdk()));
        binding.chartInstallLocation.setOnValueTouchListener(new GenericValueTouchListener(binding.chartInstallLocation, data.getInstallLocation()));
        binding.chartSignAlgorithm.setOnValueTouchListener(new GenericValueTouchListener(binding.chartSignAlgorithm, data.getSignAlgorithm()));
        binding.chartAppSource.setOnValueTouchListener(new GenericValueTouchListener(binding.chartAppSource, data.getAppSource()));

        binding.statisticsApkSize.setStatistics(data.getApkSize());
        binding.statisticsActivities.setStatistics(data.getActivites());
        binding.statisticsServices.setStatistics(data.getServices());
        binding.statisticsProviders.setStatistics(data.getProviders());
        binding.statisticsReceivers.setStatistics(data.getReceivers());
        binding.statisticsUsedPermissions.setStatistics(data.getUsedPermissions());
        binding.statisticsDefinedPermissions.setStatistics(data.getDefinedPermissions());
        binding.statisticsFiles.setStatistics(data.getFiles());
        binding.statisticsDrawables.setStatistics(data.getDrawables());
        binding.statisticsDrawablesDifferent.setStatistics(data.getDifferentDrawables());
        binding.statisticsLayouts.setStatistics(data.getLayouts());
        binding.statisticsLayoutsDifferent.setStatistics(data.getDifferentLayouts());

        binding.localStatisticsContent.setVisibility(View.VISIBLE);
        binding.loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<LocalStatisticsData> loader) {
        this.data = null;
    }

    @Override
    public void onProgressChanged(int currentProgress, int maxProgress) {
        binding.loadingBar.setProgress(currentProgress, maxProgress);
    }

    private ColumnChartData getSdkColumnChart(Map<Integer, List<String>> map, @ColorInt int columnColor) {

        List<Column> columns = new ArrayList<>(map.size());
        List<AxisValue> axisValues = new ArrayList<>(map.size());
        List<SubcolumnValue> values;

        int axisValue = 0;
        for (int sdk = 1; sdk <= AndroidVersionHelper.MAX_SDK_VERSION; sdk++) {
            if (map.get(sdk) == null)
                continue;

            int applicationCount = map.get(sdk).size();

            values = new ArrayList<>();
            values.add(new SubcolumnValue(applicationCount, columnColor));
            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);

            axisValues.add(new AxisValue(axisValue++).setLabel(String.valueOf(sdk)));
        }

        ColumnChartData data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis(axisValues).setName(getResources().getString(R.string.sdk))
                .setMaxLabelChars(3));
        return data;

    }

    private ColumnChartData getColumnChart(Map<?, List<String>> map, @StringRes int axisName, @ColorInt int columnColor) {

        List<Column> columns = new ArrayList<>(map.size());
        List<AxisValue> axisValues = new ArrayList<>(map.size());
        List<SubcolumnValue> values;

        int axisValue = 0;
        for (Map.Entry<?, List<String>> entry : map.entrySet()) {

            int applicationCount = entry.getValue().size();

            values = new ArrayList<>();
            values.add(new SubcolumnValue(applicationCount, columnColor));
            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);

            axisValues.add(new AxisValue(axisValue++).setLabel(entry.getKey().toString()));
        }

        ColumnChartData data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis(axisValues).setName(getResources().getString(axisName))
                .setMaxLabelChars(10));
        return data;

    }

    private void setupChart(ColumnChartView chartView) {
        chartView.setZoomType(ZoomType.HORIZONTAL);
        chartView.setScrollEnabled(true);
        chartView.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);
    }

    private class SdkValueTouchListener implements ColumnChartOnValueSelectListener {

        private ColumnChartView chartView;
        private Map<Integer, List<String>> data;

        SdkValueTouchListener(ColumnChartView chartView, Map<Integer, List<String>> data) {
            this.chartView = chartView;
            this.data = data;
        }

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            char[] label = chartView.getColumnChartData().getAxisXBottom().getValues().get(columnIndex).getLabelAsChars();
            String labelString = new String(label);

            int sdkInteger = Integer.valueOf(labelString);
            Toast.makeText(getActivity(), "Selected SDK: " + sdkInteger, Toast.LENGTH_SHORT).show();
            ArrayList<String> packageNames = (ArrayList<String>) data.get(sdkInteger);

            AppListDialog.newInstance(packageNames)
                    .show(((AppCompatActivity) getContext()).getSupportFragmentManager(), AppListDialog.class.getSimpleName());
        }

        @Override
        public void onValueDeselected() {
        }

    }

    private class GenericValueTouchListener implements ColumnChartOnValueSelectListener {

        private ColumnChartView chartView;
        private Map<?, List<String>> data;

        GenericValueTouchListener(ColumnChartView chartView, Map<?, List<String>> data) {
            this.chartView = chartView;
            this.data = data;
        }

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            char[] label = chartView.getColumnChartData().getAxisXBottom().getValues().get(columnIndex).getLabelAsChars();
            String labelString = new String(label);

            Toast.makeText(getActivity(), "Selected value: " + labelString, Toast.LENGTH_SHORT).show();
            ArrayList<String> packageNames = null;

            for (Map.Entry<?, List<String>> entry : data.entrySet()) {
                if (labelString.equals(entry.getKey().toString())) {
                    packageNames = (ArrayList<String>) entry.getValue();
                    break;
                }
            }

            AppListDialog.newInstance(packageNames)
                    .show(((AppCompatActivity) getContext()).getSupportFragmentManager(), AppListDialog.class.getSimpleName());
        }

        @Override
        public void onValueDeselected() {
        }

    }

}
