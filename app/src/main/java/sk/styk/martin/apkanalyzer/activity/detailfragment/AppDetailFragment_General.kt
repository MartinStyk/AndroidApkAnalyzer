package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_general.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailGeneralBinding
import sk.styk.martin.apkanalyzer.model.detail.GeneralData

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */

class AppDetailFragment_General : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAppDetailGeneralBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_app_detail_general, container, false)

        binding.data = arguments.getParcelable(AppDetailFragment.ARG_CHILD) ?: throw IllegalArgumentException("data null")

        return binding.root
    }
}
