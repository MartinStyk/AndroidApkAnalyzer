package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemApplicationBinding
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * @author Martin Styk
 * @date 23.12.2018.
 */
class AppListAdapter(private val onClickListener: AppListClickListener) :
    RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    interface AppListClickListener {
        fun onAppClick(appListData: AppListData)
    }

    var data = emptyList<AppListData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, onClickListener)
    }

    init {
        this.setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class ViewHolder(
        private val binding: ListItemApplicationBinding,
        private val onClickListener: AppListClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appData: AppListData) {
            binding.data = appData
            binding.onClickListener = onClickListener
            binding.executePendingBindings()
        }
    }
}