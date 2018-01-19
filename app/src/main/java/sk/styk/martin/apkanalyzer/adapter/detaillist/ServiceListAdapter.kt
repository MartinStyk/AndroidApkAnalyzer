package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.view.DetailListItemView

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ServiceListAdapter(items: List<ServiceData>) : GenericDetailListAdapter<ServiceData, ServiceListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_service_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.name.text = data.name
        holder.permission.valueText = data.permission
        holder.exported.valueText = data.isExported.toString()
        holder.stopWithTask.valueText = data.isStopWithTask.toString()
        holder.singleUser.valueText = data.isSingleUser.toString()
        holder.isolatedProcess.valueText = data.isolatedProcess().toString()
        holder.external.valueText = data.isExternalService.toString()
    }

    internal inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView
        val permission: DetailListItemView
        val exported: DetailListItemView
        val stopWithTask: DetailListItemView
        val singleUser: DetailListItemView
        val isolatedProcess: DetailListItemView
        val external: DetailListItemView


        init {
            name = v.findViewById(R.id.item_service_name)
            permission = v.findViewById(R.id.item_service_permission)
            exported = v.findViewById(R.id.item_service_exported)
            stopWithTask = v.findViewById(R.id.item_service_stop_with_task)
            singleUser = v.findViewById(R.id.item_service_single_user)
            isolatedProcess = v.findViewById(R.id.item_service_isolated_process)
            external = v.findViewById(R.id.item_service_external_service)

        }
    }
}