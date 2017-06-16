package sk.styk.martin.apkanalyzer.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.databinding.AppListContentBinding;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;
import sk.styk.martin.apkanalyzer.util.ResolveResource;

public class AppListRecyclerViewAdapter extends RecyclerView.Adapter<AppListRecyclerViewAdapter.ViewHolder> {

    private List<AppBasicInfo> items;
    private Context context;
    private Callback callback;

    //track which is selected and highlight it
    private int selectedPos = RecyclerView.NO_POSITION;
    private Drawable itemBackgroundSelector;
    private int selectedRowColor = -1;

    public AppListRecyclerViewAdapter(Context context, List<AppBasicInfo> items, Callback callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
        itemBackgroundSelector = ContextCompat.getDrawable(context, R.drawable.item_list_content_selector);
        selectedRowColor = ResolveResource.getColor(context, R.attr.colorAccent);
    }

    public interface Callback {
        void onItemClick(View v, AppBasicInfo appBasicInfo, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AppListContentBinding itemBinding = AppListContentBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(items.get(position));

        // highlight selected item in large screen
        if (MainActivity.mTwoPane)
            handleItemColoring(holder, position);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // highlight current item in list and remember pressed one
                if (MainActivity.mTwoPane) {
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);
                }

                callback.onItemClick(v, holder.binding.getAppInfo(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void dataChange(List<AppBasicInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    // selected app stays highlighted
    private void handleItemColoring(final ViewHolder holder, final int position) {
        if (selectedPos == position) {
            holder.itemView.setBackgroundColor(selectedRowColor);
        } else {
            // for API 15 compatibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.itemView.setBackground(itemBackgroundSelector);
            } else {
                holder.itemView.setBackgroundDrawable(itemBackgroundSelector);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final AppListContentBinding binding;

        public ViewHolder(AppListContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppBasicInfo item) {
            binding.setAppInfo(item);
            binding.executePendingBindings();
        }
    }
}