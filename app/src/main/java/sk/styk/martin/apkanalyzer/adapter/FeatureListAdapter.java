package sk.styk.martin.apkanalyzer.adapter;

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
 * Created by Martin Styk on 12.10.2017.
 */
public class FeatureListAdapter extends RecyclerView.Adapter<FeatureListAdapter.ViewHolder> {

    private final List<FeatureData> items;

    public FeatureListAdapter(@NonNull List<FeatureData> items) {
        super();
        setHasStableIds(true);
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_feature_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FeatureData data = items.get(position);
        holder.name.setText(data.getName());
        holder.required.setValue(data.isRequired());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final DetailListItemView required;

        ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.item_feature_name);
            required = (DetailListItemView) v.findViewById(R.id.item_feature_required);
        }
    }
}