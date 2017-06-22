package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * Created by Martin Styk on 18.06.2017.
 */

public class AppDetailFragment_Basic extends Fragment {

    public static final String ARG = "position_arg";

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_page1, container, false);
        AppBasicInfo data = getArguments().getParcelable(ARG);
        textView = (TextView) rootView.findViewById(R.id.item_detail);
        textView.setText(data.toString());
        return rootView;
    }
}
