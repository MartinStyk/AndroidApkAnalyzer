package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemServiceDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter


/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ServiceDetailListAdapter(override val presenter: ServiceDetailPageContract.Presenter) : GenericListAdapter<ServiceDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemServiceDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemServiceDetailBinding) : ServiceDetailPageContract.ItemView, RecyclerView.ViewHolder(binding.root) {
        override fun bind(item: ServiceData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}