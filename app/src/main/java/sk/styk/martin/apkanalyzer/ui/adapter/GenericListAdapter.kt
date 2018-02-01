package sk.styk.martin.apkanalyzer.ui.adapter

import android.support.v7.widget.RecyclerView
import sk.styk.martin.apkanalyzer.ui.ListPresenter

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
abstract class GenericListAdapter<VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    abstract val presenter: ListPresenter<VH>

    init {
        this.setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = presenter.onBindViewOnPosition(position, holder)

    override fun getItemCount(): Int = presenter.itemCount()

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position
}
