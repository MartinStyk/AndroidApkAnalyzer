package sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemAppPermissionDetailBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppPermissionListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<AppPermissionListAdapter.ViewHolder>() {

    data class DecomposedPermissionData(val completeName: String, val simpleName: String)

    private val showPermissionDetailEvent = SingleLiveEvent<DecomposedPermissionData>()
    val showPermissionDetail: LiveData<DecomposedPermissionData> = showPermissionDetailEvent

    var items = emptyList<DecomposedPermissionData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(StringDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemAppPermissionDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(PermissionDataViewModel(items[position]))
    }

    override fun getItemCount() = items.size

    inner class PermissionDataViewModel(private val permission: DecomposedPermissionData) {

        val simpleName = permission.simpleName
        val completeName = permission.completeName

        fun showDetail() {
            showPermissionDetailEvent.value = permission
        }

        fun copyName(): Boolean {
            copyToClipboardEvent.value = CopyToClipboard(TextInfo.from(completeName))
            return true
        }
    }

    inner class ViewHolder(val binding: ListItemAppPermissionDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: PermissionDataViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class StringDiffCallback(
        private val newList: List<DecomposedPermissionData>,
        private val oldList: List<DecomposedPermissionData>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
