package sk.styk.martin.apkanalyzer.activity.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 23.06.2017.
 */
class InfoDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments.getString(ARG_TITLE)
        val value = arguments.getString(ARG_VALUE)
        val description = arguments.getString(ARG_DESCRIPTION)

        return AlertDialog.Builder(activity)
                .setTitle(title + ": " + value)
                .setMessage(description)
                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
                .create()
    }

    companion object {

        private const val ARG_TITLE = "title"
        private const val ARG_VALUE = "value"
        private const val ARG_DESCRIPTION = "description"


        fun newInstance(title: String, value: String, description: String): InfoDialog {
            val frag = InfoDialog()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_VALUE, value)
            args.putString(ARG_DESCRIPTION, description)
            frag.arguments = args
            return frag
        }
    }

}
