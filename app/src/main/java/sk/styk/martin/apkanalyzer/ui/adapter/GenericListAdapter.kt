package sk.styk.martin.apkanalyzer.ui.adapter

import android.support.v7.widget.RecyclerView

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
abstract class GenericListAdapter<T, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    init {
        this.setHasStableIds(true)
    }

    abstract override fun onBindViewHolder(holder: VH, position: Int)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position
}
