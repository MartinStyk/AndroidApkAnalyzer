package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.Manifest
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import kotlinx.android.synthetic.main.fragment_app_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.FragmentAppListBinding
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppListDetailFragment
import sk.styk.martin.apkanalyzer.util.file.ApkFilePicker

/**
 * List of all applications
 *
 * @author Martin Styk
 */

class AppListFragment : Fragment() {

    private lateinit var binding: FragmentAppListBinding
    private lateinit var viewModel: AppListViewModel

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AppListViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAppListBinding.inflate(LayoutInflater.from(context), container, false)
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_analyze_not_installed.setOnClickListener { startFilePicker(true) }


        binding.viewModel = viewModel

        viewModel.appListData.observe(this, Observer {
            viewModel.dataChanged(it)
        })

        viewModel.appClicked.observe(this, Observer {
            if (it != null) {
                val parentFragment = parentFragment as AppListDetailFragment
                parentFragment.itemClicked(it.packageName)
            }
        })

        viewModel.isFilterActive.observe(this, Observer { isActive ->
            if (isActive == true && (snackbar == null || snackbar?.isShown == false))
                snackbar = Snackbar.make(app_list_container, R.string.app_filtering_active, Snackbar.LENGTH_INDEFINITE).apply {
                    show()

            } else if (isActive == false) {
                snackbar?.dismiss()
                snackbar = null
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val searchMenuItem = menu?.findItem(R.id.action_search)
        val searchView = searchMenuItem?.actionView as? SearchView
        searchView?.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.filterOnAppName(if (!TextUtils.isEmpty(newText)) newText else null)
                    return true
                }

                override fun onQueryTextSubmit(p0: String?) = true
            })
            queryHint = getString(R.string.action_search)
            setOnCloseListener {
                if (!TextUtils.isEmpty(query)) {
                    setQuery(null, true)
                }
                false
            }

            if (!viewModel.filterComponent.name.isNullOrBlank()) {
                setQuery(viewModel.filterComponent.name, false)
            }
        }

        menu?.findItem(
                when (viewModel.filterComponent.source) {
                    AppSource.GOOGLE_PLAY -> R.id.menu_show_google_play_apps
                    AppSource.AMAZON_STORE -> R.id.menu_show_amazon_store_apps
                    AppSource.SYSTEM_PREINSTALED -> R.id.menu_show_system_pre_installed_apps
                    AppSource.UNKNOWN -> R.id.menu_show_unknown_source_apps
                    else -> R.id.menu_show_all_apps
                }
        )?.isChecked = true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_analyze_not_installed -> startFilePicker()
            R.id.menu_show_all_apps -> {
                item.isChecked = true
                viewModel.filterOnAppSource(null)
            }
            R.id.menu_show_google_play_apps -> {
                item.isChecked = true
                viewModel.filterOnAppSource(AppSource.GOOGLE_PLAY)
            }
            R.id.menu_show_amazon_store_apps -> {
                item.isChecked = true
                viewModel.filterOnAppSource(AppSource.AMAZON_STORE)
            }
            R.id.menu_show_system_pre_installed_apps -> {
                item.isChecked = true
                viewModel.filterOnAppSource(AppSource.SYSTEM_PREINSTALED)
            }
            R.id.menu_show_unknown_source_apps -> {
                item.isChecked = true
                viewModel.filterOnAppSource(AppSource.UNKNOWN)
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
        if (requestCode == ApkFilePicker.REQUEST_PICK_APK && resultCode == RESULT_OK && data?.data != null) {
            val parentFragment = parentFragment as AppListDetailFragment
            parentFragment.itemSelectedFromFile(data.data)
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
    private fun startFilePicker(withPermissionCheck: Boolean = true) {
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

    companion object {
        private const val REQUEST_STORAGE_PERMISSIONS = 13245
        fun newInstance() = AppListFragment()
    }
}
