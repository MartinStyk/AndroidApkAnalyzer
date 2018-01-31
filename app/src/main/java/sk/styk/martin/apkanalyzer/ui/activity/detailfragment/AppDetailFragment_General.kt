package sk.styk.martin.apkanalyzer.ui.activity.detailfragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerFragment
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailGeneralBinding

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */

class AppDetailFragment_General : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAppDetailGeneralBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_app_detail_general, container, false)

        binding.data = arguments.getParcelable(AppDetailPagerFragment.ARG_CHILD) ?: throw IllegalArgumentException("data null")

        return binding.root
    }
}