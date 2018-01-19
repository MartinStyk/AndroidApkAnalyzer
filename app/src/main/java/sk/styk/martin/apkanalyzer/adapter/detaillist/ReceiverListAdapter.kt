package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.view.DetailListItemView

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ReceiverListAdapter(items: List<BroadcastReceiverData>) : GenericDetailListAdapter<BroadcastReceiverData, ReceiverListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_receiver_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.name.text = data.name
        holder.permission.valueText = data.permission
        holder.exported.valueText = data.isExported.toString()

    }

    internal inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView
        val permission: DetailListItemView
        val exported: DetailListItemView

        init {
            name = v.findViewById(R.id.item_receiver_name)
            permission = v.findViewById(R.id.item_receiver_permission)
            exported = v.findViewById(R.id.item_receiver_exported)
        }
    }
}