package sk.styk.martin.apkanalyzer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData;
import sk.styk.martin.apkanalyzer.view.DetailListItemView;

/**
 * Created by Martin Styk on 07.07.2017.
 */
public class ReceiverListAdapter extends RecyclerView.Adapter<ReceiverListAdapter.ViewHolder> {

    private final List<BroadcastReceiverData> items;

    public ReceiverListAdapter(@NonNull List<BroadcastReceiverData> items) {
        super();
        setHasStableIds(true);
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_receiver_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BroadcastReceiverData data = items.get(position);
        holder.name.setText(data.getName());
        holder.permission.setValue(data.getPermission());
        holder.exported.setValue(data.isExported());

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
        final DetailListItemView permission;
        final DetailListItemView exported;

        ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.item_receiver_name);
            permission = (DetailListItemView) v.findViewById(R.id.item_receiver_permission);
            exported = (DetailListItemView) v.findViewById(R.id.item_receiver_exported);
        }
    }
}