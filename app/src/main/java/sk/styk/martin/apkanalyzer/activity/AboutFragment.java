package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.BuildConfig;
import sk.styk.martin.apkanalyzer.R;


public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ((TextView) rootView.findViewById(R.id.about_app_version)).setText(BuildConfig.VERSION_NAME);
        ((TextView) rootView.findViewById(R.id.about_app_github_link)).setMovementMethod(LinkMovementMethod.getInstance());

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Hide action bar item for searching
        menu.clear();

        super.onCreateOptionsMenu(menu, inflater);
    }
}
