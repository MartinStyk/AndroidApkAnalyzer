package sk.styk.martin.apkanalyzer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.databinding.ItemListContentBinding;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    //track which is selected and highlight it
    private int selectedPos = RecyclerView.NO_POSITION;

    private List<AppBasicInfo> items;
    private Context context;

    public SimpleItemRecyclerViewAdapter(Context context, List<AppBasicInfo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemListContentBinding itemBinding = ItemListContentBinding.inflate(layoutInflater, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(items.get(position));

        if (MainActivity.mTwoPane && selectedPos == position) {
            holder.itemView.setBackgroundColor(Color.BLUE);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.mTwoPane) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, position);
                    context.startActivity(intent);
                } else {
                    // highlight current item in list
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);

                    // show details fragment
                    Bundle arguments = new Bundle();
                    arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, position);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void dataChange(List<AppBasicInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemListContentBinding binding;


        public ViewHolder(ItemListContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppBasicInfo item) {
            binding.setAppInfo(item);
            binding.executePendingBindings();
        }
    }
}