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
import sk.styk.martin.apkanalyzer.util.file.AppOperations;

/**
 * @author Martin Styk
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ((TextView) rootView.findViewById(R.id.about_app_version)).setText(BuildConfig.VERSION_NAME);
        ((TextView) rootView.findViewById(R.id.about_app_github_link)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) rootView.findViewById(R.id.about_app_privacy_policy)).setMovementMethod(LinkMovementMethod.getInstance());

        rootView.findViewById(R.id.about_app_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AppOperations().openGooglePlay(getContext(), getContext().getPackageName());
            }
        });

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
