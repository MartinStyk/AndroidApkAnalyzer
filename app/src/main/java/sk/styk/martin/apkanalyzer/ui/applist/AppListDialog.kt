package sk.styk.martin.apkanalyzer.ui.applist

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class AppListDialog : DialogFragment() {

//    private lateinit var presenter: AppListContract.Presenter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        presenter = AppListPresenter(AppListFromPackageNamesLoader(requireContext(), arguments?.getStringArrayList(PACKAGES_ARGUMENT) ?: emptyList()), LoaderManager.getInstance(this))
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.dialog_app_list, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        presenter.view = this
//        presenter.initialize(arguments!!)
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        return AlertDialog.Builder(requireContext())
//                .setView(R.layout.dialog_app_list)
//                .setTitle(R.string.apps)
//                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
//                .create()
//    }
//
//    override fun setUpViews() {
//        dialog?.recycler_view_applications?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//    }
//
//    override fun hideLoading() {
//        dialog?.list_view_progress_bar?.visibility = View.GONE
//    }
//
//    override fun nothingToDisplay() {
//        dialog?.list_view_progress_bar?.visibility = View.GONE
//        dialog?.nothing_to_show?.visibility = View.VISIBLE
//    }
//
//    override fun showAppList() {
//        dialog?.recycler_view_applications?.adapter = AppListRecyclerAdapter(presenter)
//    }
//
//    override fun openAppDetailActivity(packageName: String) {
//        requireContext().startActivity(AppDetailActivity.createIntent(packageName = packageName, context = requireContext()))
//    }

    companion object {
        fun newInstance(packageNames: ArrayList<String>): AppListDialog {
            val frag = AppListDialog()
            val args = Bundle()
            frag.arguments = args
            return frag
        }
    }
}