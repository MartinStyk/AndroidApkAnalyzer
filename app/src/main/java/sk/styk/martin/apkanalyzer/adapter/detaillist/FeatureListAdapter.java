package sk.styk.martin.apkanalyzer.adapter.detaillist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.model.detail.FeatureData;
import sk.styk.martin.apkanalyzer.view.DetailListItemView;

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
public class FeatureListAdapter extends GenericDetailListAdapter<FeatureData, FeatureListAdapter.ViewHolder> {

    public FeatureListAdapter(@NonNull List<FeatureData> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_feature_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FeatureData data = getItem(position);
        holder.name.setText(data.getName());
        holder.required.setValue(data.isRequired());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final DetailListItemView required;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.item_feature_name);
            required = v.findViewById(R.id.item_feature_required);
        }
    }
}