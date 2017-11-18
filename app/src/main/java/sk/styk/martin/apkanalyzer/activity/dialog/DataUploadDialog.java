package sk.styk.martin.apkanalyzer.activity.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.util.ConnectivityHelper;

/**
 * @author Martin Styk
 * @version 23.06.2017.
 */
public class DataUploadDialog extends DialogFragment {

    public static DataUploadDialog newInstance() {
        return new DataUploadDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getString(R.string.upload_dialog_title);
        String message = getString(R.string.settings_allow_metadata_upload_description);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConnectivityHelper.setConnectionAllowedByUser(getContext(), true);
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConnectivityHelper.setConnectionAllowedByUser(getContext(), false);
                        dismiss();
                    }
                })
                .create();
    }

}