package sk.styk.martin.apkanalyzer.ui.activity.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sk.styk.martin.apkanalyzer.R

class SimpleTextDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(ARG_TITLE) ?: ""
        val message = arguments?.getString(ARG_MESSAGE) ?: ""

        return AlertDialog.Builder(requireContext())
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

        fun newInstance(title: String, value: String, message: String): SimpleTextDialog {
            val frag = SimpleTextDialog()
            val args = Bundle()
            args.putString(ARG_TITLE, title + ": " + value)
            args.putString(ARG_MESSAGE, message)
            frag.arguments = args
            return frag
        }
    }


}
