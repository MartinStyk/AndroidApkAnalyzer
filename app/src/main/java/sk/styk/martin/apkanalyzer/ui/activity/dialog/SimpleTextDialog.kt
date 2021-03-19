package sk.styk.martin.apkanalyzer.ui.activity.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.DialogComponent

class SimpleTextDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogData = requireNotNull(arguments?.getParcelable<DialogComponent>(ARG_DATA))

        val context = requireContext()
        val builder = AlertDialog.Builder(context)
                .setTitle(dialogData.title.getText(context))
                .setMessage(dialogData.message.getText(context))

        if (dialogData.positiveButtonText != null) {
            builder.setPositiveButton(dialogData.positiveButtonText.getText(context)) { _, _ -> dismiss() }
        }

        if (dialogData.negativeButtonText != null) {
            builder.setNegativeButton(dialogData.negativeButtonText.getText(context)) { _, _ -> dismiss() }
        }

        return builder.create()
    }

    companion object {

        private const val ARG_DATA = "data"

        fun newInstance(title: String, message: String): SimpleTextDialog {
            return newInstance(DialogComponent(TextInfo.from(title), TextInfo.from(message)))
        }

        fun newInstance(title: String, value: String, message: String): SimpleTextDialog {
            return newInstance(DialogComponent(TextInfo.from("$title: $value"), TextInfo.from(message)))
        }

        fun newInstance(dialogComponent: DialogComponent): SimpleTextDialog {
            return SimpleTextDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DATA, dialogComponent)
                }
            }
        }

    }
}
