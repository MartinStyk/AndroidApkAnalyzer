package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.content.ComponentName
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.view.DetailListItemView

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ActivityListAdapter(items: List<ActivityData>) : GenericDetailListAdapter<ActivityData, ActivityListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_activity_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.name.text = data.name
        holder.label.valueText = data.label
        holder.parent.valueText = data.parentName
        holder.permission.valueText = data.permission

        if (data.isExported) {
            holder.run.isEnabled = true
            holder.run.setOnClickListener { v ->
                val intent = Intent()
                intent.component = ComponentName(data.packageName, data.name)
                try {
                    v.context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(v.context, R.string.activity_run_failed, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            holder.run.isEnabled = false
        }
    }

    internal inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView
        val label: DetailListItemView
        val parent: DetailListItemView
        val permission: DetailListItemView
        val run: Button

        init {
            name = v.findViewById(R.id.item_activity_name)
            label = v.findViewById(R.id.item_activity_label)
            parent = v.findViewById(R.id.item_activity_parent)
            permission = v.findViewById(R.id.item_activity_permission)
            run = v.findViewById(R.id.item_activity_run)
        }
    }
}