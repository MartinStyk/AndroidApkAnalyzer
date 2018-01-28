package sk.styk.martin.apkanalyzer.ui.activity.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 23.06.2017.
 */
class SimpleTextDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments.getString(ARG_TITLE)
        val message = arguments.getString(ARG_MESSAGE)

        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
                .create()
    }

    companion object {

        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "value"

        fun newInstance(title: String, message: String): SimpleTextDialog {
            val frag = SimpleTextDialog()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            frag.arguments = args
            return frag
        }
    }


}
