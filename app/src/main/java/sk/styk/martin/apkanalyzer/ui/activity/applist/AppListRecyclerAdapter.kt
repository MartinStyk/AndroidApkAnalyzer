package sk.styk.martin.apkanalyzer.ui.activity.applist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemApplicationBinding
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.ui.adapter.GenericListAdapter

/**
 * App list adapter for recycler view.
 * Used in AppListDialog
 *
 * @author Martin Styk
 * @version 05.01.2017.
 */
class AppListRecyclerAdapter(override val presenter: AppListContract.Presenter) : GenericListAdapter<AppListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemApplicationBinding) : RecyclerView.ViewHolder(binding.root), AppListContract.ItemView {
        init {
            binding.root.setOnClickListener { _ -> presenter.onAppClick(adapterPosition) }
        }

        override fun bind(appData: AppListData) {
            binding.data = appData
            binding.executePendingBindings()
        }
    }
}