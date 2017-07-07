package sk.styk.martin.apkanalyzer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;

/**
 * Created by Martin Styk on 06.07.2017.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private List<String> allItems = new ArrayList<>();
    private Context context;
    private boolean initialLoad = true;

    public FileListAdapter(List<String> initialData) {
        allItems.addAll(initialData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_item_files_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (allItems != null && position < allItems.size()) {
            holder.filePath.setText(allItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return allItems == null ? 0 : allItems.size();
    }

    public void appendItems(List<String> items) {
        int oldItemCount = getItemCount();
        allItems.addAll(items);
        notifyItemRangeInserted(oldItemCount, items.size());

    }


    public void clear() {
        allItems.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView filePath;

        ViewHolder(View v) {
            super(v);
            filePath = (TextView) v.findViewById(R.id.item_detail_file);
        }
    }
}