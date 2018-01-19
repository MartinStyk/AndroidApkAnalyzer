package sk.styk.martin.apkanalyzer.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailActivity
import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailPagerFragment
import sk.styk.martin.apkanalyzer.adapter.detaillist.GenericDetailListAdapter
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

/**
 * Permission list adapter used in LocalPermissionFragment
 *
 *
 * @author Martin Styk
 * @version 13.01.2017.
 */
class PermissionListAdapter(items: List<LocalPermissionData>) : GenericDetailListAdapter<LocalPermissionData, PermissionListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_permission_local_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.permissionName.text = data.permissionData.name
        holder.permissionSimpleName.text = data.permissionData.simpleName
        holder.affectedApps.text = holder.view.context.getString(R.string.permissions_number_apps, data.permissionStatusList.size)
    }

    internal inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val permissionName: TextView
        val permissionSimpleName: TextView
        val affectedApps: TextView

        init {
            permissionName = view.findViewById(R.id.permission_name)
            permissionSimpleName = view.findViewById(R.id.permission_simple_name)
            affectedApps = view.findViewById(R.id.affected_apps)

            view.setOnClickListener { view ->
                val intent = Intent(view.context, PermissionDetailActivity::class.java)
                intent.putExtra(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA, getItem(adapterPosition))
                view.context.startActivity(intent)
            }
        }
    }
}