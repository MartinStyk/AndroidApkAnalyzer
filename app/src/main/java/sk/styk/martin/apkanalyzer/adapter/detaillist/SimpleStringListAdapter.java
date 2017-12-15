package sk.styk.martin.apkanalyzer.adapter.detaillist;

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
public class SimpleStringListAdapter extends GenericDetailListAdapter<String, SimpleStringListAdapter.ViewHolder> {

    public SimpleStringListAdapter(@NonNull List<String> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple_string, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.item_detail_string);
        }
    }
}