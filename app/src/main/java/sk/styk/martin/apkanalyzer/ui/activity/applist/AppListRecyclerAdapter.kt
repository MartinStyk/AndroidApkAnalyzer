package sk.styk.martin.apkanalyzer.ui.activity.applist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemApplicationBinding
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter

class AppListRecyclerAdapter(override val presenter: AppListContract.Presenter) : GenericListAdapter<AppListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemApplicationBinding) : RecyclerView.ViewHolder(binding.root), AppListContract.ItemView {
        override fun bind(appData: AppListData) {
            binding.apply {
                root.setOnClickListener { presenter.onAppClick(appData) }
                data = appData
                executePendingBindings()
            }
        }
    }
}