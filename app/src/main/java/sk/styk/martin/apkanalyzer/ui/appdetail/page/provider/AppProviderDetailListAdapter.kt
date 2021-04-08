package sk.styk.martin.apkanalyzer.ui.appdetail.page.provider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemProviderDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppProviderDetailListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<AppProviderDetailListAdapter.ViewHolder>() {

    data class ExpandedContentProviderData(val contentProviderData: ContentProviderData, val expanded: Boolean)

    private val providerUpdateEvent = SingleLiveEvent<ExpandedContentProviderData>()
    val providerUpdate: LiveData<ExpandedContentProviderData> = providerUpdateEvent

    var items = emptyList<ExpandedContentProviderData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(ProviderDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemProviderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ProviderDataViewModel(items[position]))
    }

    override fun getItemCount() = items.size

    inner class ProviderDataViewModel(private val expandedProviderData: ExpandedContentProviderData) {

        val name = expandedProviderData.contentProviderData.name.substring(expandedProviderData.contentProviderData.name.lastIndexOf(".") + 1)
        val packageName = expandedProviderData.contentProviderData.name.substring(0, expandedProviderData.contentProviderData.name.lastIndexOf("."))
        val expanded = expandedProviderData.expanded

        val authority = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_authority),
                value = TextInfo.from(expandedProviderData.contentProviderData.authority),
                description = TextInfo.from(R.string.provider_authority_description)
        )
        val readPermission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_read_permission),
                value = if (expandedProviderData.contentProviderData.readPermission != null) TextInfo.from(expandedProviderData.contentProviderData.readPermission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.provider_read_permission_description)
        )
        val writePermission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_write_permission),
                value = if (expandedProviderData.contentProviderData.writePermission != null) TextInfo.from(expandedProviderData.contentProviderData.writePermission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.provider_write_permission_description)
        )
        val exported = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_exported),
                value = TextInfo.from(if (expandedProviderData.contentProviderData.isExported) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.provider_exported_description)
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = Description.from(detailInfo)
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = CopyToClipboard.from(detailInfo)
            return true
        }

        fun onTitleLongClick() : Boolean {
            copyToClipboardEvent.value = CopyToClipboard(TextInfo.from(expandedProviderData.contentProviderData.name))
            return true
        }

        fun toggleExpanded(newlyExpanded: Boolean) {
            providerUpdateEvent.value = expandedProviderData.copy(expanded = newlyExpanded)
        }
    }

    inner class ViewHolder(val binding: ListItemProviderDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ProviderDataViewModel) {
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

    private inner class ProviderDiffCallback(private val newList: List<ExpandedContentProviderData>,
                                             private val oldList: List<ExpandedContentProviderData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].contentProviderData == newList[newItemPosition].contentProviderData
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}