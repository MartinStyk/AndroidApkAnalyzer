package sk.styk.martin.apkanalyzer.adapter.detaillist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
public abstract class GenericDetailListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final List<T> items;

    public GenericDetailListAdapter(@NonNull List<T> items) {
        super();
        setHasStableIds(true);
        this.items = items;
    }

    @Override
    public abstract void onBindViewHolder(final VH holder, final int position);

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
