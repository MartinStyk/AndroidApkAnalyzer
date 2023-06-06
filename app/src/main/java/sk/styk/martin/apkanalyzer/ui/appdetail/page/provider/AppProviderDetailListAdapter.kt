package sk.styk.martin.apkanalyzer.ui.appdetail.page.provider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemProviderDetailBinding
import sk.styk.martin.apkanalyzer.databinding.ListItemProviderDetailExpandedBinding
import sk.styk.martin.apkanalyzer.core.appanalysis.model.ContentProviderData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.recycler.ExpandableItemViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.recycler.LazyExpandableViewHolder
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

    inner class ProviderDataViewModel(private val expandedProviderData: ExpandedContentProviderData) : ExpandableItemViewModel {

        val name = expandedProviderData.contentProviderData.name.substring(expandedProviderData.contentProviderData.name.lastIndexOf(".") + 1)
        val packageName = expandedProviderData.contentProviderData.name.substring(0, expandedProviderData.contentProviderData.name.lastIndexOf("."))
        override val expanded = expandedProviderData.expanded

        val authority = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.provider_authority),
            value = expandedProviderData.contentProviderData.authority.orNone(),
            description = TextInfo.from(R.string.provider_authority_description),
        )
        val readPermission = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.provider_read_permission),
            value = expandedProviderData.contentProviderData.readPermission.orNone(),
            description = TextInfo.from(R.string.provider_read_permission_description),
        )
        val writePermission = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.provider_write_permission),
            value = expandedProviderData.contentProviderData.writePermission.orNone(),
            description = TextInfo.from(R.string.provider_write_permission_description),
        )
        val exported = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.provider_exported),
            value = TextInfo.from(if (expandedProviderData.contentProviderData.isExported) R.string.yes else R.string.no),
            description = TextInfo.from(R.string.provider_exported_description),
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = Description.from(detailInfo)
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = CopyToClipboard.from(detailInfo)
            return true
        }

        fun onTitleLongClick(): Boolean {
            copyToClipboardEvent.value = CopyToClipboard(TextInfo.from(expandedProviderData.contentProviderData.name))
            return true
        }

        override fun toggleExpanded(newlyExpanded: Boolean) {
            providerUpdateEvent.value = expandedProviderData.copy(expanded = newlyExpanded)
        }
    }

    inner class ViewHolder(binding: ListItemProviderDetailBinding) :
        LazyExpandableViewHolder<ListItemProviderDetailBinding, ListItemProviderDetailExpandedBinding, ProviderDataViewModel>(binding) {

        override fun baseContainer() = baseBinding.container

        override fun expandedInflation() = ListItemProviderDetailExpandedBinding.inflate(LayoutInflater.from(baseBinding.root.context))

        override fun expandableContainer() = expandedBinding.expandableContainer

        override fun toggleArrow() = baseBinding.toggleArrow

        override fun headerContainer() = baseBinding.headerContainer
    }

    private inner class ProviderDiffCallback(
        private val newList: List<ExpandedContentProviderData>,
        private val oldList: List<ExpandedContentProviderData>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].contentProviderData == newList[newItemPosition].contentProviderData
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
