package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.PermissionData;
import sk.styk.martin.apkanalyzer.model.ServiceData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class AppDetailFragment_Permission extends Fragment {

    private WebView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_page1, container, false);
        PermissionData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);
        textView = (WebView) rootView.findViewById(R.id.item_detail);
        String string = String.format("<html><body> %s </body></html>", data.toString());
        textView.loadData(string, "text/html; charset=utf-8", "utf-8");
        return rootView;
    }
}
