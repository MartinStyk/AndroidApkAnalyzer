package sk.styk.martin.apkanalyzer.ui.activity.detailfragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailCertificateBinding

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
class AppDetailFragment_Certificate : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAppDetailCertificateBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_app_detail_certificate, container, false)

        binding.data = arguments.getParcelable(AppDetailFragment.ARG_CHILD) ?: throw IllegalArgumentException("data null")

        return binding.root
    }
}
