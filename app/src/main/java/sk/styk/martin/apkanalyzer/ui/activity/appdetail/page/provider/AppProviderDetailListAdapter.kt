package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemProviderDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppProviderDetailListAdapter @Inject constructor() : RecyclerView.Adapter<AppProviderDetailListAdapter.ViewHolder>() {

    private val openDescriptionEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val openDescription: LiveData<DetailInfoAdapter.DetailInfo> = openDescriptionEvent

    private val copyToClipboardEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val copyToClipboard: LiveData<DetailInfoAdapter.DetailInfo> = copyToClipboardEvent

    var items = emptyList<ContentProviderData>()
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

    inner class ProviderDataViewModel(private val providerData: ContentProviderData) {

        val name = providerData.name
        val authority = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_authority),
                value = TextInfo.from(providerData.authority),
                description = TextInfo.from(R.string.provider_authority_description)
        )
        val readPermission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_read_permission),
                value = if (providerData.readPermission != null) TextInfo.from(providerData.readPermission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.provider_read_permission_description)
        )
        val writePermission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_write_permission),
                value = if (providerData.writePermission != null) TextInfo.from(providerData.writePermission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.provider_write_permission_description)
        )
        val exported = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.provider_exported),
                value = TextInfo.from(if (providerData.isExported) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.provider_exported_description)
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = detailInfo
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = detailInfo
            return true
        }
    }

    inner class ViewHolder(val binding: ListItemProviderDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ProviderDataViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class ProviderDiffCallback(private val newList: List<ContentProviderData>,
                                             private val oldList: List<ContentProviderData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}