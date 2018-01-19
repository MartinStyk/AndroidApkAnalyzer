package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import sk.styk.martin.apkanalyzer.view.DetailListItemView

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
class FeatureListAdapter(items: List<FeatureData>) : GenericDetailListAdapter<FeatureData, FeatureListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feature_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.name.text = data.name
        holder.required.valueText = data.isRequired.toString()
    }

    internal inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView
        val required: DetailListItemView

        init {
            name = v.findViewById(R.id.item_feature_name)
            required = v.findViewById(R.id.item_feature_required)
        }
    }
}