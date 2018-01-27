package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repackaged_detection.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.presenter.RepackagedDetectionPresenter


/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class RepackagedDetectionFragment : Fragment(), RepackagedDetectionView {
    private lateinit var presenter: RepackagedDetectionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = RepackagedDetectionPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_repackaged_detection, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        if (savedInstanceState == null) {
            performDetection();
        }
    }

    override fun onResume() {
        super.onResume()
        this.presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        this.presenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.presenter.destroy()
    }


    override fun showLoading() {
        repackaged_loading_data.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        repackaged_loading_data.visibility = View.GONE
    }

    override fun showAppOk() {
        repackaged_image.setImageResource(R.drawable.ic_ok)
        repackaged_header.text = getString(R.string.repackaged_result_ok)
    }

    override fun showAppNotOk() {
        repackaged_image.setImageResource(R.drawable.ic_warning)
        repackaged_header.text = getString(R.string.repackaged_result_nok)
    }

    override fun showAppNotDetected() {
        repackaged_image.setImageResource(R.drawable.ic_android)
        repackaged_header.text = getString(R.string.repackaged_result_insufficient)
    }

    override fun showNoInternetConnection() {
        repackaged_image.setImageResource(R.drawable.ic_cloud_upload)
        repackaged_header.text = getString(R.string.no_internet_connection)
        repackaged_description.text = getString(R.string.no_internet_connection_description)
    }

    override fun showUploadNotAllowed() {
        repackaged_image.setImageResource(R.drawable.ic_allow_upload)
        repackaged_header.text = getString(R.string.metadata_upload_not_allowed)
        repackaged_description.text = getString(R.string.metadata_upload_not_allowed_description)
    }

    override fun showDetectionError() {
        repackaged_image.setImageResource(R.drawable.ic_not_available)
        repackaged_header.text = getString(R.string.repackaged_error)
        repackaged_description.text = getString(R.string.repackaged_error_description)
    }

    override fun showServiceUnavailable() {
        repackaged_image.setImageResource(R.drawable.ic_not_available)
        repackaged_header.text = getString(R.string.service_not_available)
        repackaged_description.text = getString(R.string.service_not_available_description)
    }

    private fun performDetection() {
        presenter.initialize(currentData())
    }

    private fun currentData(): AppDetailData {
        return arguments.getParcelable(DATA)
    }


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