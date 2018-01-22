package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_resource.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailCertificateBinding
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailResourceBinding
import sk.styk.martin.apkanalyzer.model.detail.ResourceData

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */
class AppDetailFragment_Resource : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAppDetailResourceBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_app_detail_resource, container, false)

        binding.data = arguments.getParcelable(AppDetailFragment.ARG_CHILD) ?: throw IllegalArgumentException("data null")

        return binding.root
    }
}
