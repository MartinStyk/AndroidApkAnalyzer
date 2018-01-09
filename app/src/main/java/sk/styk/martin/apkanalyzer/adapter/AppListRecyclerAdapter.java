package sk.styk.martin.apkanalyzer.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailActivity;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.adapter.detaillist.GenericDetailListAdapter;
import sk.styk.martin.apkanalyzer.model.list.AppListData;

/**
 * App list adapter for recycler view.
 * Used in AppListDialog
 *
 * Created by Martin Styk on 05.01.2017.
 */
public class AppListRecyclerAdapter extends GenericDetailListAdapter<AppListData, AppListRecyclerAdapter.ViewHolder> {

    public AppListRecyclerAdapter(@NonNull List<AppListData> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AppListData data = getItem(position);
        holder.icon.setImageDrawable(data.getIcon());
        holder.applicationName.setText(data.getApplicationName());
        holder.packageName.setText(data.getPackageName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView applicationName;
        final TextView packageName;

        ViewHolder(View v) {
            super(v);
            icon = v.findViewById(R.id.package_img);
            applicationName = v.findViewById(R.id.application_name);
            packageName = v.findViewById(R.id.package_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AppDetailActivity.class);
                    intent.putExtra(AppDetailFragment.ARG_PACKAGE_NAME, getItem(getAdapterPosition()).getPackageName());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}