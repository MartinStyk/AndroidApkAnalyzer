package sk.styk.martin.apkanalyzer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.dummy.DummyContent;

import java.util.List;

public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    //track which is selected and highlight it
    private int selectedPos = RecyclerView.NO_POSITION;

    private final List<DummyContent.DummyItem> mValues;
    private Context context;

    public SimpleItemRecyclerViewAdapter(Context context, List<DummyContent.DummyItem> items) {
        this.context = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(sk.styk.martin.apkanalyzer.R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        if(MainActivity.mTwoPane && selectedPos == position){
            holder.itemView.setBackgroundColor(Color.BLUE);
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.mTwoPane){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    context.startActivity(intent);
                }else {
                    // highlight current item in list
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);

                    // show details fragment
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(sk.styk.martin.apkanalyzer.R.id.item_detail_container, fragment).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyContent.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(sk.styk.martin.apkanalyzer.R.id.id);
            mContentView = (TextView) view.findViewById(sk.styk.martin.apkanalyzer.R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}