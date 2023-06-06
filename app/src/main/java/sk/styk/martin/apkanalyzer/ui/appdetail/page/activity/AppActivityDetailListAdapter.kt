package sk.styk.martin.apkanalyzer.ui.appdetail.page.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemActivityDetailBinding
import sk.styk.martin.apkanalyzer.databinding.ListItemActivityDetailExpandedBinding
import sk.styk.martin.apkanalyzer.core.appanalysis.model.ActivityData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.recycler.ExpandableItemViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.recycler.LazyExpandableViewHolder
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(ActivityDataViewModel(items[position]))

    override fun getItemCount() = items.size

    inner class ActivityDataViewModel(private val expandedActivityData: ExpandedActivityData) : ExpandableItemViewModel {

        val name = expandedActivityData.activityData.name.substring(expandedActivityData.activityData.name.lastIndexOf(".") + 1)
        val packageName = expandedActivityData.activityData.name.substring(0, expandedActivityData.activityData.name.lastIndexOf("."))
        override val expanded = expandedActivityData.expanded

        val labelDetailItemInfo = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.activity_label),
            value = expandedActivityData.activityData.label.orNa(),
            description = TextInfo.from(R.string.activity_label_description),
        )
        val parentDetailItemInfo = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.activity_parent),
            value = expandedActivityData.activityData.parentName.orNa(),
            description = TextInfo.from(R.string.activity_parent_description),
        )
        val permissionDetailItemInfo = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.activity_permission),
            value = expandedActivityData.activityData.permission.orNa(),
            description = TextInfo.from(R.string.activity_permission_description),
        )

        val runButtonVisible = expandedActivityData.activityData.isExported

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = Description.from(detailInfo)
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = CopyToClipboard.from(detailInfo)
            return true
        }

        fun onTitleLongClick(): Boolean {
            copyToClipboardEvent.value = CopyToClipboard(TextInfo.from(expandedActivityData.activityData.name))
            return true
        }

        fun onRunClick() {
            runActivityEvent.value = expandedActivityData.activityData
        }

        override fun toggleExpanded(newlyExpanded: Boolean) {
            activityUpdateEvent.value = expandedActivityData.copy(expanded = newlyExpanded)
        }
    }

    inner class ViewHolder(binding: ListItemActivityDetailBinding) :
        LazyExpandableViewHolder<ListItemActivityDetailBinding, ListItemActivityDetailExpandedBinding, ActivityDataViewModel>(binding) {

        override fun baseContainer() = baseBinding.container

        override fun expandedInflation() = ListItemActivityDetailExpandedBinding.inflate(LayoutInflater.from(baseBinding.root.context))

        override fun expandableContainer() = expandedBinding.expandableContainer

        override fun toggleArrow() = baseBinding.toggleArrow

        override fun headerContainer() = baseBinding.headerContainer
    }

    private inner class ActivityDiffCallback(
        private val newList: List<ExpandedActivityData>,
        private val oldList: List<ExpandedActivityData>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].activityData == newList[newItemPosition].activityData
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
