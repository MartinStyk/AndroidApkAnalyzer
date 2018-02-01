package sk.styk.martin.apkanalyzer.ui.activity.permission.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.adapter.GenericListAdapter

/**
 * Permission list adapter used in LocalPermissionFragment
 *
 * @author Martin Styk
 * @version 13.01.2017.
 */
class PermissionListAdapter(override val presenter: LocalPermissionsContract.Presenter) : GenericListAdapter<PermissionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_permission_local_data, parent, false))

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), LocalPermissionsContract.ItemView {
        override var permissionName = ""
            set(value) {
                field = value
                view.findViewById<TextView>(R.id.permission_name).text = value
            }

        override var permissionSimpleName = ""
            set(value) {
                field = value
                view.findViewById<TextView>(R.id.permission_simple_name).text = value
            }
        override var affectedApps = 0
            set(value) {
                field = value
                view.findViewById<TextView>(R.id.affected_apps).text = view.context.getString(R.string.permissions_number_apps, value)
            }

        init {
            view.setOnClickListener { _ -> presenter.permissionSelected(adapterPosition) }
        }
    }
}