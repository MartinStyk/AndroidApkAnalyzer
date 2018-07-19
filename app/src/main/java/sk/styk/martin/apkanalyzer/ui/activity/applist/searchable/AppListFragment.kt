package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ListFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ListView
import android.widget.SearchView
import kotlinx.android.synthetic.main.fragment_app_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.AppListLoader
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppListDetailFragment
import sk.styk.martin.apkanalyzer.util.file.ApkFilePicker

/**
 * List of all applications
 *
 * @author Martin Styk
 */
class AppListFragment : ListFragment(), SearchView.OnQueryTextListener, SearchView.OnCloseListener, LoaderManager.LoaderCallbacks<List<AppListData>> {

    private lateinit var listAdapter: AppListAdapter

    private lateinit var searchView: SearchView

    private var isListShown: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_analyze_not_installed.setOnClickListener { startFilePicker(true) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        listAdapter = AppListAdapter(context)

        if (savedInstanceState == null) {
            setHasOptionsMenu(true)
            setListAdapter(listAdapter)
            setListShown(false)

            loaderManager.initLoader(AppListLoader.ID, null, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Show an action bar item for searching.
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setEnabled(true)?.isVisible = true

        searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = getString(R.string.action_search)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val currentFilter = if (!TextUtils.isEmpty(newText)) newText else null
        listAdapter.filterOnAppName(currentFilter)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onClose(): Boolean {
        if (!TextUtils.isEmpty(searchView.query)) {
            searchView.setQuery(null, true)
        }
        return true
    }

    override fun onListItemClick(listView: ListView?, view: View?, position: Int, id: Long) {
        val parentFragment = parentFragment as AppListDetailFragment
        val appBasicData = AppListData::class.java.cast(view!!.tag)
        parentFragment.itemClicked(appBasicData.packageName, null)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<AppListData>> {
        return AppListLoader(requireContext())
    }

    override fun onLoadFinished(loader: Loader<List<AppListData>>, data: List<AppListData>) {
        listAdapter.clear()
        listAdapter.addAll(data)

        if (isResumed) setListShown(true) else setListShownNoAnimation(true)
    }

    override fun onLoaderReset(loader: Loader<List<AppListData>>) {
        listAdapter.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_analyze_not_installed -> startFilePicker(true)
            R.id.menu_show_all_apps -> {
                item.isChecked = true
                listAdapter.filterOnAppSource(null)
            }
            R.id.menu_show_google_play_apps -> {
                item.isChecked = true
                listAdapter.filterOnAppSource(AppSource.GOOGLE_PLAY)
            }
            R.id.menu_show_amazon_store_apps -> {
                item.isChecked = true
                listAdapter.filterOnAppSource(AppSource.AMAZON_STORE)
            }
            R.id.menu_show_system_pre_installed_apps -> {
                item.isChecked = true
                listAdapter.filterOnAppSource(AppSource.SYSTEM_PREINSTALED)
            }
            R.id.menu_show_unknown_source_apps -> {
                item.isChecked = true
                listAdapter.filterOnAppSource(AppSource.UNKNOWN)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Currently it is called only after APK file is selected from storage
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // handle picked apk file and
        if (requestCode == ApkFilePicker.REQUEST_PICK_APK && resultCode == RESULT_OK) {
            val parentFragment = parentFragment as AppListDetailFragment
            parentFragment.itemClicked(null, ApkFilePicker.getPathFromIntentData(data?.data))
        }
    }

    /**
     * This is called only when storage permission is granted as we use permission granting only when
     * reading APK file
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFilePicker(false)
            } else {
                Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Start file picker activity in order to select APK file from storage.
     *
     * @param withPermissionCheck if true, permissions are requested
     */
    private fun startFilePicker(withPermissionCheck: Boolean) {
        if (withPermissionCheck) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSIONS)
            } else {
                startFilePicker(false)
            }
        } else {
            try {
                startActivityForResult(ApkFilePicker.filePickerIntent, ApkFilePicker.REQUEST_PICK_APK)
            } catch (exception: ActivityNotFoundException) {
                Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.activity_not_found_browsing, Snackbar.LENGTH_LONG).show()
            }

        }
    }


    override fun setListShown(shown: Boolean) {
        setListShown(shown, true)
    }

    override fun setListShownNoAnimation(shown: Boolean) {
        setListShown(shown, false)
    }

    private fun setListShown(shown: Boolean, animate: Boolean) {
        if (isListShown == shown) {
            return
        }
        isListShown = shown
        if (shown) {
            if (animate) {
                list_view_progress_bar.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_out))
                list_view_list.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_in))
            }
            list_view_progress_bar.visibility = View.GONE
            list_view_list.visibility = View.VISIBLE
        } else {
            if (animate) {
                list_view_progress_bar.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_in))
                list_view_list.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_out))
            }
            list_view_progress_bar.visibility = View.VISIBLE
            list_view_list.visibility = View.INVISIBLE
        }
    }

    companion object {

        private const val REQUEST_STORAGE_PERMISSIONS = 13245
    }
}
