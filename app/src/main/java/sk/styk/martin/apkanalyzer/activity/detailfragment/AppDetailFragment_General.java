package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.AppDetailData;
import sk.styk.martin.apkanalyzer.model.GeneralData;
import sk.styk.martin.apkanalyzer.view.DetailItemView;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class AppDetailFragment_General extends Fragment {

    private DetailItemView detailView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_page1, container, false);
        GeneralData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);
        detailView = (DetailItemView) rootView.findViewById(R.id.item_detail);
        detailView.setTitle("Title");
        detailView.setValue(data.toString());
        return rootView;
    }
}
