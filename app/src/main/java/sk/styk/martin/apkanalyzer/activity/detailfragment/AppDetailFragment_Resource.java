package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.ResourceData;

/**
 * Created by Martin Styk on 03.07.2017.
 */
public class AppDetailFragment_Resource extends Fragment {

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_page1, container, false);
        ResourceData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);
        textView = (TextView) rootView.findViewById(R.id.item_detail);
        textView.setText(data.toString());
        return rootView;
    }
}
