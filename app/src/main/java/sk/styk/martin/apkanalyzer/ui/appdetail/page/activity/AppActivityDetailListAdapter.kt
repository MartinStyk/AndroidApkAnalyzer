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


const val ARROW_ANIMATION_DURATION = 500L

const val ROTATION_STANDARD = 0f
const val ROTATION_FLIPPED = 180f

class AppActivityDetailListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<AppActivityDetailListAdapter.ViewHolder>() {

    data class ExpandedActivityData(val activityData: ActivityData, val expanded: Boolean)

    private val runActivityEvent = SingleLiveEvent<ActivityData>()
    val runActivity: LiveData<ActivityData> = runActivityEvent

    private val activityUpdateEvent = SingleLiveEvent<ExpandedActivityData>()
    val activityUpdate: LiveData<ExpandedActivityData> = activityUpdateEvent

    var items = emptyList<ExpandedActivityData>()
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

    inner class ActivityDataViewModel(private val expandedActivityData: ExpandedActivityData) {

        val name = expandedActivityData.activityData.name.substring(expandedActivityData.activityData.name.lastIndexOf(".") + 1)
        val packageName = expandedActivityData.activityData.name.substring(0, expandedActivityData.activityData.name.lastIndexOf("."))
        val expanded = expandedActivityData.expanded

        val labelDetailItemInfo = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_label),
                value = if (expandedActivityData.activityData.label != null) TextInfo.from(expandedActivityData.activityData.label) else TextInfo.from(R.string.NA),
                description = TextInfo.from(R.string.activity_label_description)
        )
        val parentDetailItemInfo = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_parent),
                value = if (expandedActivityData.activityData.parentName != null) TextInfo.from(expandedActivityData.activityData.parentName) else TextInfo.from(R.string.NA),
                description = TextInfo.from(R.string.activity_parent_description)
        )
        val permissionDetailItemInfo = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_permission),
                value = if (expandedActivityData.activityData.permission != null) TextInfo.from(expandedActivityData.activityData.permission) else TextInfo.from(R.string.NA),
                description = TextInfo.from(R.string.activity_permission_description)
        )

        val runButtonVisible = expandedActivityData.activityData.isExported

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = detailInfo
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = detailInfo
            return true
        }

        fun onRunClick() {
            runActivityEvent.value = expandedActivityData.activityData
        }

        fun toggleExpanded(newlyExpanded: Boolean) {
            activityUpdateEvent.value = expandedActivityData.copy(expanded = newlyExpanded)
        }

    }

    inner class ViewHolder(val binding: ListItemActivityDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ActivityDataViewModel) {
            binding.viewModel = viewModel
            updateExpandedState(viewModel.expanded)
            binding.headerContainer.setOnClickListener {
                val newlyExpanded = !binding.expandableContainer.isExpanded
                viewModel.toggleExpanded(newlyExpanded)
                binding.expandableContainer.isExpanded = newlyExpanded
                binding.toggleArrow.animate().setDuration(ARROW_ANIMATION_DURATION).rotation(if (newlyExpanded) ROTATION_FLIPPED else ROTATION_STANDARD)
            }
        }

        private fun updateExpandedState(expanded: Boolean) {
            binding.expandableContainer.setExpanded(expanded, false)
            binding.toggleArrow.rotation = if (expanded) ROTATION_FLIPPED else ROTATION_STANDARD
        }

    }

    private inner class ActivityDiffCallback(private val newList: List<ExpandedActivityData>,
                                             private val oldList: List<ExpandedActivityData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].activityData == newList[newItemPosition].activityData
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}