package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
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

        binding.chartMinSdk.setZoomType(ZoomType.HORIZONTAL);
        binding.chartMinSdk.setScrollEnabled(true);
        binding.chartMinSdk.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        return binding.getRoot();
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
}
