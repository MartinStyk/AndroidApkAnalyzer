package sk.styk.martin.apkanalyzer.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailActivity
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.adapter.detaillist.GenericDetailListAdapter
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * App list adapter for recycler view.
 * Used in AppListDialog
 *
 * @author Martin Styk
 * @version 05.01.2017.
 */
class AppListRecyclerAdapter(items: List<AppListData>) : GenericDetailListAdapter<AppListData, AppListRecyclerAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_application, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.icon.setImageDrawable(data.icon)
        holder.applicationName.text = data.applicationName
        holder.packageName.text = data.packageName
    }

    internal inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView
        val applicationName: TextView
        val packageName: TextView

        init {
            icon = v.findViewById(R.id.package_img)
            applicationName = v.findViewById(R.id.application_name)
            packageName = v.findViewById(R.id.package_name)

            v.setOnClickListener { view ->
                val intent = Intent(view.context, AppDetailActivity::class.java)
                intent.putExtra(AppDetailFragment.ARG_PACKAGE_NAME, getItem(adapterPosition).packageName)
                view.context.startActivity(intent)
            }
        }
    }
}