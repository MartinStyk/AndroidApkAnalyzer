package sk.styk.martin.apkanalyzer.ui.applist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.core.applist.model.LazyAppListData
import sk.styk.martin.apkanalyzer.databinding.ListItemApplicationBinding
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import java.lang.ref.WeakReference
import javax.inject.Inject

class AppListAdapter @Inject constructor() : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    val appClicked = SingleLiveEvent<AppListClickData>()

    var data = emptyList<LazyAppListData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(AppDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class ViewHolder(private val binding: ListItemApplicationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appData: LazyAppListData) {
            binding.data = appData
            binding.root.setOnClickListener { appClicked.value = AppListClickData(WeakReference(it), appData) }
        }
    }

    private inner class AppDiffCallback(
        private val newList: List<LazyAppListData>,
        private val oldList: List<LazyAppListData>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].packageName == newList[newItemPosition].packageName
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].packageName == newList[newItemPosition].packageName
    }

    data class AppListClickData(val view: WeakReference<View>, val lazyAppListData: LazyAppListData)
}
