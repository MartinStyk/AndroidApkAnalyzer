package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.business.task.LocalStatisticsLoader;
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalStatisticsBinding;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData;

public class LocalStatisticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<LocalStatisticsData> {

    FragmentLocalStatisticsBinding binding;
    LocalStatisticsData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocalStatisticsBinding.inflate(inflater);
        getLoaderManager().initLoader(LocalStatisticsLoader.ID, null, this);

        return binding.getRoot();
    }

    @Override
    public Loader<LocalStatisticsData> onCreateLoader(int id, Bundle args) {
        return new LocalStatisticsLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<LocalStatisticsData> loader, LocalStatisticsData data) {
        this.data = data;
        binding.localStatistics.setText(data.toString());
    }

    @Override
    public void onLoaderReset(Loader<LocalStatisticsData> loader) {
        this.data = null;
    }
}
