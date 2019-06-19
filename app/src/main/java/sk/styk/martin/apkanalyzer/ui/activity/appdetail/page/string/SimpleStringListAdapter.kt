package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemSimpleStringBinding
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class SimpleStringListAdapter(override val presenter: StringListDetailPageContract.Presenter) : GenericListAdapter<SimpleStringListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemSimpleStringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemSimpleStringBinding) : StringListDetailPageContract.ItemView, RecyclerView.ViewHolder(binding.root) {
        override fun bind(item: String) {
            binding.value = item
            binding.executePendingBindings()
        }
    }
}