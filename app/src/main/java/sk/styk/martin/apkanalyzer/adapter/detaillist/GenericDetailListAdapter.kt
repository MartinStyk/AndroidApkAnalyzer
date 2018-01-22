package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
abstract class GenericDetailListAdapter<T, VH : RecyclerView.ViewHolder>(private val items: List<T>) : RecyclerView.Adapter<VH>() {

    init {
        this.setHasStableIds(true)
    }

    abstract override fun onBindViewHolder(holder: VH, position: Int)

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    fun getItem(position: Int): T = items[position]

    override fun getItemViewType(position: Int): Int = position
}
