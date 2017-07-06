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
import sk.styk.martin.apkanalyzer.model.CertificateData;

/**
 * Created by Martin Styk on 22.06.2017.
 */
public class AppDetailFragment_Certificate extends Fragment {

    private WebView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_app_detail_page1, container, false);
        CertificateData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);
        textView = (WebView) rootView.findViewById(R.id.item_detail);
        String string = String.format("<html><body> %s </body></html>", data.toString());
        textView.loadData(string, "text/html; charset=utf-8", "utf-8");
        return rootView;
    }
}
