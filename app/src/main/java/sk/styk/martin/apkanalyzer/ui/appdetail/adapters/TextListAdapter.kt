package sk.styk.martin.apkanalyzer.ui.appdetail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemSimpleStringBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import javax.inject.Inject

class TextListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<TextListAdapter.TextViewHolder>() {

    var items: List<TextInfo> = emptyList()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(StringDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val itemBinding = ListItemSimpleStringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TextViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(TextItemViewModel(items[position]))
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class TextItemViewModel(val text: TextInfo) {

        fun onLongClick(): Boolean {
            return true
        }
    }

    inner class TextViewHolder(val binding: ListItemSimpleStringBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(textViewModel: TextItemViewModel) {
            binding.viewModel = textViewModel
        }
    }

    private inner class StringDiffCallback(
        private val newList: List<TextInfo>,
        private val oldList: List<TextInfo>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
