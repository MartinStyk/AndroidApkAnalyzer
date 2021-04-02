package sk.styk.martin.apkanalyzer.ui.appdetail.page.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemActivityDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppActivityDetailListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<AppActivityDetailListAdapter.ViewHolder>() {

    private val runActivityEvent = SingleLiveEvent<ActivityData>()
    val runActivity: LiveData<ActivityData> = runActivityEvent

    var items = emptyList<ActivityData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(ActivityDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemActivityDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ActivityDataViewModel(items[position]))
    }

    override fun getItemCount() = items.size

    inner class ActivityDataViewModel(private val activityData: ActivityData) {

        val name = activityData.name
        val labelDetailItemInfo = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_label),
                value = if (activityData.label != null) TextInfo.from(activityData.label) else TextInfo.from(R.string.NA),
                description = TextInfo.from(R.string.activity_label_description)
        )
        val parentDetailItemInfo = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_parent),
                value = if (activityData.parentName != null) TextInfo.from(activityData.parentName) else TextInfo.from(R.string.NA),
                description = TextInfo.from(R.string.activity_parent_description)
        )
        val permissionDetailItemInfo = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_permission),
                value = if (activityData.permission != null) TextInfo.from(activityData.permission) else TextInfo.from(R.string.NA),
                description = TextInfo.from(R.string.activity_permission_description)
        )

        val runButtonVisible = activityData.isExported

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = detailInfo
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = detailInfo
            return true
        }

        fun onRunClick() {
            runActivityEvent.value = activityData
        }
    }

    inner class ViewHolder(val binding: ListItemActivityDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ActivityDataViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class ActivityDiffCallback(private val newList: List<ActivityData>,
                                             private val oldList: List<ActivityData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}