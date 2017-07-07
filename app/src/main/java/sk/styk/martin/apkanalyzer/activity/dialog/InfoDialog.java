package sk.styk.martin.apkanalyzer.activity.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * @author Martin Styk
 * @version 23.06.2017.
 */
public class InfoDialog extends DialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_VALUE = "value";
    private static final String ARG_DESCRIPTION = "description";


    public static InfoDialog newInstance(String title, String value, String description) {
        InfoDialog frag = new InfoDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_VALUE, value);
        args.putString(ARG_DESCRIPTION, description);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        String value = getArguments().getString(ARG_VALUE);
        String description = getArguments().getString(ARG_DESCRIPTION);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title + ": " + value)
                .setMessage(description)
                .create();
    }

}
