package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.AppDetailData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class AppDetailFragment_Activity extends Fragment {

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_page1, container, false);
        AppDetailData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);
        textView = (TextView) rootView.findViewById(R.id.item_detail);
        textView.setText(data.getActivityData().toString());
        return rootView;
    }
}
