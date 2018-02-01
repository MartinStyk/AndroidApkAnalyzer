package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.business.analysis.task.AppDetailLoader
import sk.styk.martin.apkanalyzer.business.upload.task.AppDataUploadTask
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.general.CertificateDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.general.GeneralDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_PATH
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PAGER_PAGE
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.activity.ActivityDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.detailfragment.AppDetailFragment_Classes
import sk.styk.martin.apkanalyzer.ui.activity.detailfragment.AppDetailFragment_Feature
import sk.styk.martin.apkanalyzer.ui.activity.detailfragment.AppDetailFragment_Provider
import sk.styk.martin.apkanalyzer.ui.activity.detailfragment.AppDetailFragment_Receiver
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.general.ResourceDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.detailfragment.AppDetailFragment_Service
import java.util.*


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class AppDetailPagerPresenter(
        val loader: Loader<AppDetailData?>,
        val loaderManager: LoaderManager
) : LoaderManager.LoaderCallbacks<AppDetailData?>, AppDetailPagerContract.Presenter {

    override lateinit var view: AppDetailPagerContract.View

    private var packageName: String? = null
    private var pathToPackage: String? = null

    private var appDetailData: AppDetailData? = null

    override fun initialize(bundle: Bundle) {
        packageName = bundle.getString(ARG_PACKAGE_NAME)
        pathToPackage = bundle.getString(ARG_PACKAGE_PATH)
        view.setUpViews()
        loaderManager.initLoader(AppDetailLoader.ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<AppDetailData?> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<AppDetailData?>?, appDetailData: AppDetailData?) {
        this.appDetailData = appDetailData
        view.hideLoading()

        if (appDetailData == null) {
            view.showLoadingFailed()

        } else {
            view.showAppDetails(packageName = appDetailData.generalData.packageName, icon = appDetailData.generalData.icon);

            AppDataUploadTask().execute(appDetailData)
        }
    }

    override fun onLoaderReset(loader: Loader<AppDetailData?>) {
        appDetailData = null
    }

    override fun actionButtonClick() {
        // show actions only when data is loaded
        appDetailData?.let {
            view.showActionDialog(it)
        }
    }


    override fun getGeneralDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelable(ARG_PAGER_PAGE, appDetailData?.generalData)
        val fragment = GeneralDetailFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getCertificateDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelable(ARG_PAGER_PAGE, appDetailData?.certificateData)
        val fragment = CertificateDetailFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getResourceDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelable(ARG_PAGER_PAGE, appDetailData?.resourceData)
        val fragment = ResourceDetailFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getActivityDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelableArrayList(ARG_PAGER_PAGE, appDetailData?.activityData as ArrayList<out Parcelable>)
        val fragment = ActivityDetailPageFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getServiceDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelableArrayList(ARG_PAGER_PAGE, appDetailData?.serviceData as ArrayList<out Parcelable>)
        val fragment = AppDetailFragment_Service()
        fragment.arguments = args
        return fragment
    }

    override fun getProviderDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelableArrayList(ARG_PAGER_PAGE, appDetailData?.contentProviderData as ArrayList<out Parcelable>)
        val fragment = AppDetailFragment_Provider()
        fragment.arguments = args
        return fragment
    }

    override fun getReceiverDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelableArrayList(ARG_PAGER_PAGE, appDetailData?.broadcastReceiverData as ArrayList<out Parcelable>)
        val fragment = AppDetailFragment_Receiver()
        fragment.arguments = args
        return fragment
    }

    override fun getFeatureDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelableArrayList(ARG_PAGER_PAGE, appDetailData?.featureData as ArrayList<out Parcelable>)
        val fragment = AppDetailFragment_Feature()
        fragment.arguments = args
        return fragment
    }

    override fun getUsedPermissionDetailsFragment(): Fragment {

        return getFeatureDetailsFragment()
//
//        val args = Bundle()
//        args.putStringArrayList(ARG_PAGER_PAGE, appDetailData?.permissionData?.usesPermissionsNames as ArrayList<String>)
//        val fragment = AppDetailFragment_Permission()
//        fragment.arguments = args
//        return fragment
    }

    override fun getDefinedPermissionDetailsFragment(): Fragment {
        return getGeneralDetailsFragment()

//        val args = Bundle()
//        args.putStringArrayList(ARG_PAGER_PAGE, appDetailData?.permissionData?.definesPermissionsNames as ArrayList<String>)
//        val fragment = AppDetailFragment_Permission()
//        fragment.arguments = args
//        return fragment
    }

    override fun getClasspathDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelable(ARG_PAGER_PAGE, appDetailData?.classPathData)
        val fragment = AppDetailFragment_Classes()
        fragment.arguments = args
        return fragment
    }

}
