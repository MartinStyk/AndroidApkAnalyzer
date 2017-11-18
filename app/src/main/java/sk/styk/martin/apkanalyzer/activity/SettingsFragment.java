package sk.styk.martin.apkanalyzer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.BuildConfig;
import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.util.ConnectivityHelper;


public class SettingsFragment extends Fragment {

    private CheckBox allowUpload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        ((TextView) rootView.findViewById(R.id.settings_upload_explanation)).setMovementMethod(LinkMovementMethod.getInstance());

        allowUpload = rootView.findViewById(R.id.allow_upload);
        allowUpload.setChecked(ConnectivityHelper.isConnectionAllowedByUser(getContext()));
        allowUpload.setOnCheckedChangeListener(
                new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        ConnectivityHelper.setConnectionAllowedByUser(getContext(), b);
                    }
                }
        );

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
