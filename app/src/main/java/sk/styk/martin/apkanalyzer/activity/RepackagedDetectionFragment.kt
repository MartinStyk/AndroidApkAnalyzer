package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repackaged_detection.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionStatus


/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class RepackagedDetectionFragment : Fragment(), LoaderManager.LoaderCallbacks<RepackagedDetectionLoader.LoaderResult> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(RepackagedDetectionLoader.ID, arguments, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<RepackagedDetectionLoader.LoaderResult> {
        return RepackagedDetectionLoader(args.getParcelable(DATA), context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_repackaged_detection, null, false)
    }

    override fun onLoadFinished(loader: Loader<RepackagedDetectionLoader.LoaderResult>, result: RepackagedDetectionLoader.LoaderResult) {
        repackaged_loading_data.visibility = View.GONE

        when (result) {
            is RepackagedDetectionLoader.LoaderResult.Success -> {
                when (result.result.status) {
                    RepackagedDetectionStatus.NOK -> {
                        repackaged_image.setImageResource(R.drawable.ic_warning)
                        repackaged_header.text = getString(R.string.repackaged_result_nok)
                    }
                    RepackagedDetectionStatus.OK -> {
                        repackaged_image.setImageResource(R.drawable.ic_ok)
                        repackaged_header.text = getString(R.string.repackaged_result_ok)
                    }
                    RepackagedDetectionStatus.INSUFFICIENT_DATA -> {
                        repackaged_image.setImageResource(R.drawable.ic_android)
                        repackaged_header.text = getString(R.string.repackaged_result_insufficient)
                    }
                }
                repackaged_header.text = result.result.toString()
            }
            is RepackagedDetectionLoader.LoaderResult.NotConnectedToInternet -> {
                repackaged_image.setImageResource(R.drawable.ic_cloud_upload)
                repackaged_header.text = getString(R.string.no_internet_connection)
                repackaged_description.text = getString(R.string.no_internet_connection_description)
            }
            is RepackagedDetectionLoader.LoaderResult.UserNotAllowedUpload -> {
                repackaged_image.setImageResource(R.drawable.ic_allow_upload)
                repackaged_header.text = getString(R.string.metadata_upload_not_allowed)
                repackaged_description.text = getString(R.string.metadata_upload_not_allowed_description)
            }
            is RepackagedDetectionLoader.LoaderResult.ServiceNotAvailable -> {
                repackaged_image.setImageResource(R.drawable.ic_not_available)
                repackaged_header.text = getString(R.string.service_not_available)
                repackaged_description.text = getString(R.string.service_not_available_description)
            }
            is RepackagedDetectionLoader.LoaderResult.CommunicationError -> {
                repackaged_image.setImageResource(R.drawable.ic_not_available)
                repackaged_header.text = getString(R.string.repackaged_error)
                repackaged_description.text = getString(R.string.repackaged_error_description)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<RepackagedDetectionLoader.LoaderResult>) {}

    companion object {
        const val TAG = "RepackagedDetectionFragment"

        private const val DATA = "data"

        fun newInstance(data: AppDetailData): RepackagedDetectionFragment {
            val frag = RepackagedDetectionFragment()
            val args = Bundle()
            args.putParcelable(DATA, data)
            frag.arguments = args
            return frag
        }
    }
}