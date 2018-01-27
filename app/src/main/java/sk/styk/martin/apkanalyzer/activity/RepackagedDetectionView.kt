package sk.styk.martin.apkanalyzer.activity

import android.app.Fragment
import android.content.Context
import android.support.v4.app.LoaderManager

/**
 * Created by Martin Styk on 27.01.2018.
 */
interface RepackagedDetectionView {

    fun showLoading()

    fun hideLoading()

    fun showAppOk()

    fun showAppNotOk()

    fun showAppNotDetected()

    fun showNoInternetConnection()

    fun showUploadNotAllowed()

    fun showDetectionError()

    fun showServiceUnavailable()

    fun getContext(): Context

    fun getLoaderManager(): LoaderManager

}