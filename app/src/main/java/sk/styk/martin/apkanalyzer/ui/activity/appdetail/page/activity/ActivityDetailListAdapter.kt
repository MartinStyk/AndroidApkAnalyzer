package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemActivityDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ActivityDetailListAdapter(override val presenter: ActivityDetailPageContract.Presenter) : GenericListAdapter<ActivityDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemActivityDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemActivityDetailBinding) : ActivityDetailPageContract.ItemView, RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemActivityRun.setOnClickListener { presenter.runActivity(adapterPosition) }
        }

        override fun bind(item: ActivityData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }

}