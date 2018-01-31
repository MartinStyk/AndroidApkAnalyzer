package sk.styk.martin.apkanalyzer.ui.activity.detailfragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerFragment
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailResourceBinding

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */
class AppDetailFragment_Resource : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAppDetailResourceBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_app_detail_resource, container, false)

        binding.data = arguments.getParcelable(AppDetailPagerFragment.ARG_CHILD) ?: throw IllegalArgumentException("data null")

        return binding.root
    }
}
