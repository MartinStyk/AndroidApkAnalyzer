package sk.styk.martin.apkanalyzer.ui.permission.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemPermissionLocalDataBinding
import sk.styk.martin.apkanalyzer.core.apppermissions.model.LocalPermissionData
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import java.lang.ref.WeakReference
import javax.inject.Inject

class PermissionListAdapter @Inject constructor() : RecyclerView.Adapter<PermissionListAdapter.ViewHolder>() {

    private val openPermissionEvent = SingleLiveEvent<PermissionClickData>()
    val openPermission: LiveData<PermissionClickData> = openPermissionEvent

    var permissions = emptyList<LocalPermissionData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(PermissionDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = permissions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPermissionLocalDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(PermissionItemViewModel(permissions[position]))
    }

    inner class PermissionItemViewModel(val permissionData: LocalPermissionData) {
        fun onClick(view: View) {
            openPermissionEvent.value = PermissionClickData(WeakReference(view), permissionData)
        }
    }

    inner class ViewHolder(private val binding: ListItemPermissionLocalDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun update(viewModel: PermissionItemViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class PermissionDiffCallback(
        private val newList: List<LocalPermissionData>,
        private val oldList: List<LocalPermissionData>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].permissionData.name == newList[newItemPosition].permissionData.name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

    data class PermissionClickData(val view: WeakReference<View>, val localPermissionData: LocalPermissionData)
}
