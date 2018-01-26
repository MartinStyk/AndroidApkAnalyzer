package sk.styk.martin.apkanalyzer.activity.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_repackaged_detection.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData

/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class RepackagedDetectionDialog : DialogFragment(), LoaderManager.LoaderCallbacks<String> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(RepackagedDetectionLoader.ID, arguments, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_repackaged_detection, container, false)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<String> {
        return RepackagedDetectionLoader(args.getParcelable(DATA), context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context)
                .setView(R.layout.dialog_repackaged_detection)
                .setTitle(R.string.detect_repackaged)
                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
                .create()
    }

    override fun onLoadFinished(loader: Loader<String>, data: String) {
        dialog.textual_output.text = data
    }

    override fun onLoaderReset(loader: Loader<String>) {}

    companion object {

        private const val DATA = "data"

        fun newInstance(data: AppDetailData): RepackagedDetectionDialog {
            val frag = RepackagedDetectionDialog()
            val args = Bundle()
            args.putParcelable(DATA, data)
            frag.arguments = args
            return frag
        }
    }
}