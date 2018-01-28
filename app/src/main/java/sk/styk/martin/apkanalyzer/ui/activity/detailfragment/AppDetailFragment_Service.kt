package sk.styk.martin.apkanalyzer.ui.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_service.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.ui.adapter.detaillist.ServiceListAdapter
import sk.styk.martin.apkanalyzer.model.detail.ServiceData

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class AppDetailFragment_Service : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail_service, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments.getParcelableArrayList<ServiceData>(AppDetailFragment.ARG_CHILD)
                ?: throw IllegalArgumentException("data null")

        recycler_view_service.adapter = ServiceListAdapter(data)
        recycler_view_service.setHasFixedSize(true)
    }
}
