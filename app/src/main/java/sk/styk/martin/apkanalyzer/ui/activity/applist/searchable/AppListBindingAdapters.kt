package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.databinding.BindingAdapter
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView


/**
 * @author Martin Styk
 * @date 23.12.2018.
 */

object AppListBindingAdapters {
    @JvmStatic
    @BindingAdapter("appListAdapter")
    fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: AppListAdapter?) {
        if (adapter != null && recyclerView.adapter == null) {
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            recyclerView.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter("show")
    fun bindFabIsShown(fab: FloatingActionButton, show: Boolean?) {
        if (show == true && !fab.isShown) {
            fab.show()
        } else if (show == false && fab.isShown) {
            fab.hide()
        }
    }

}
