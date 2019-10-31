package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemApplicationBinding
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent

class AppListAdapter: RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    val appClicked = SingleLiveEvent<AppListData>()

    var data = emptyList<AppListData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        this.setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class ViewHolder(
        private val binding: ListItemApplicationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appData: AppListData) {
            binding.data = appData
            binding.root.setOnClickListener { appClicked.value = appData }
            binding.executePendingBindings()
        }
    }
}