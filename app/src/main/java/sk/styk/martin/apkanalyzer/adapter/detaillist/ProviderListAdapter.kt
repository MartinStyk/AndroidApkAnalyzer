package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.view.DetailListItemView

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ProviderListAdapter(items: List<ContentProviderData>) : GenericDetailListAdapter<ContentProviderData, ProviderListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_provider_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.name.text = data.name
        holder.authority.valueText = data.authority
        holder.readPermission.valueText = data.readPermission
        holder.writePermission.valueText = data.writePermission
        holder.exported.valueText = data.isExported.toString()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.item_provider_name)
        val authority: DetailListItemView = v.findViewById(R.id.item_provider_authority)
        val readPermission: DetailListItemView = v.findViewById(R.id.item_provider_read_permission)
        val writePermission: DetailListItemView = v.findViewById(R.id.item_provider_write_permission)
        val exported: DetailListItemView = v.findViewById(R.id.item_provider_exported)
    }
}