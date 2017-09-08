package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.LocalStatisticsLoader;
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalStatisticsBinding;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData;
import sk.styk.martin.apkanalyzer.util.AndroidVersionHelper;
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter;
import sk.styk.martin.apkanalyzer.util.PercentagePair;

public class LocalStatisticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<LocalStatisticsData> {

    FragmentLocalStatisticsBinding binding;
    LocalStatisticsData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocalStatisticsBinding.inflate(inflater);
        getLoaderManager().initLoader(LocalStatisticsLoader.ID, null, this);

        setHasOptionsMenu(true);

        binding.chartMinSdk.setZoomType(ZoomType.HORIZONTAL);
        binding.chartMinSdk.setScrollEnabled(true);
        binding.chartMinSdk.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Hide action bar item for searching
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setEnabled(false).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<LocalStatisticsData> onCreateLoader(int id, Bundle args) {
        return new LocalStatisticsLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<LocalStatisticsData> loader, LocalStatisticsData data) {
        this.data = data;

        binding.itemAnalyzeSuccess.setValue(data.getAnalyzeSuccess().getCount().toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.getAnalyzeSuccess().getPercentage()) + "%)");
        binding.itemAnalyzeFailed.setValue(data.getAnalyzeFailed().getCount().toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.getAnalyzeFailed().getPercentage()) + "%)");
        binding.itemSystemApps.setValue(data.getSystemApps().getCount().toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.getSystemApps().getPercentage()) + "%)");

        binding.chartMinSdk.setColumnChartData(getSdkColumnChart(data.getMinSdk(), getResources().getColor(R.color.colorPrimary)));
        binding.chartTargetSdk.setColumnChartData(getSdkColumnChart(data.getTargetSdk(), getResources().getColor(R.color.colorPrimary)));
        binding.chartInstallLocation.setColumnChartData(getStringBasedColumnChart(data.getInstallLocation(), R.string.install_loc, getResources().getColor(R.color.colorPrimary)));
        binding.chartSignAlgorithm.setColumnChartData(getStringBasedColumnChart(data.getSignAlgorithm(), R.string.sign_algorithm, getResources().getColor(R.color.colorPrimary)));


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
        binding.localStatisticsLoading.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<LocalStatisticsData> loader) {
        this.data = null;
    }

    private ColumnChartData getSdkColumnChart(Map<Integer, PercentagePair> map, @ColorInt int columnColor) {

        List<Column> columns = new ArrayList<>(map.size());
        List<AxisValue> axisValues = new ArrayList<>(map.size());
        List<SubcolumnValue> values;

        int axisValue = 0;
        for (int sdk = 1; sdk <= AndroidVersionHelper.MAX_SDK_VERSION; sdk++) {
            if (map.get(sdk) == null)
                continue;

            int applicationCount = map.get(sdk).getCount().intValue();

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

    private ColumnChartData getStringBasedColumnChart(Map<String, PercentagePair> map, @StringRes int axisName, @ColorInt int columnColor) {

        List<Column> columns = new ArrayList<>(map.size());
        List<AxisValue> axisValues = new ArrayList<>(map.size());
        List<SubcolumnValue> values;

        int axisValue = 0;
        for (Map.Entry<String, PercentagePair> entry : map.entrySet()) {

            int applicationCount = entry.getValue().getCount().intValue();

            values = new ArrayList<>();
            values.add(new SubcolumnValue(applicationCount, columnColor));
            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);

            axisValues.add(new AxisValue(axisValue++).setLabel(entry.getKey()));
        }

        ColumnChartData data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis(axisValues).setName(getResources().getString(axisName))
                .setMaxLabelChars(10));
        return data;

    }
}
