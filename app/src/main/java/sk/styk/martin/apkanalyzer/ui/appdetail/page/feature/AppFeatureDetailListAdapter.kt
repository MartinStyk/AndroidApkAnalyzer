package sk.styk.martin.apkanalyzer.ui.appdetail.page.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemFeatureDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import javax.inject.Inject

class AppFeatureDetailListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<AppFeatureDetailListAdapter.ViewHolder>() {

    var items = emptyList<FeatureData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(FeatureDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemFeatureDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(FeatureDataViewModel(items[position]))
    }

    override fun getItemCount() = items.size

    inner class FeatureDataViewModel(private val featureData: FeatureData) {

        val name = featureData.name

        val exported = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.feature_required),
                value = TextInfo.from(if (featureData.isRequired) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.feature_required_description)
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = detailInfo
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = detailInfo
            return true
        }

    }

    inner class ViewHolder(val binding: ListItemFeatureDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: FeatureDataViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class FeatureDiffCallback(private val newList: List<FeatureData>,
                                            private val oldList: List<FeatureData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}