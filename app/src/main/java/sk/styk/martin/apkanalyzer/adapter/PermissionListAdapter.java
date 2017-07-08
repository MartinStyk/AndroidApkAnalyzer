package sk.styk.martin.apkanalyzer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;

/**
 * Created by Martin Styk on 07.07.2017.
 */
public class PermissionListAdapter extends RecyclerView.Adapter<PermissionListAdapter.ViewHolder> {

    private final List<String> items;

    public PermissionListAdapter(@NonNull List<String> items) {
        super();
        setHasStableIds(true);
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_permission_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;

        ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.item_permission_name);
        }
    }
}