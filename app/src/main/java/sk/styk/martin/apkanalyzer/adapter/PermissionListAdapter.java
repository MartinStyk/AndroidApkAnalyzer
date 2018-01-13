package sk.styk.martin.apkanalyzer.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailActivity;
import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailPagerFragment;
import sk.styk.martin.apkanalyzer.adapter.detaillist.GenericDetailListAdapter;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData;

/**
 * Permission list adapter used in LocalPermissionFragment
 * <p>
 * Created by Martin Styk on 13.01.2017.
 */
public class PermissionListAdapter extends GenericDetailListAdapter<LocalPermissionData, PermissionListAdapter.ViewHolder> {

    public PermissionListAdapter(@NonNull List<LocalPermissionData> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_permission_local_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final LocalPermissionData data = getItem(position);
        holder.permissionName.setText(data.getPermissionData().getName());
        holder.permissionSimpleName.setText(data.getPermissionData().getSimpleName());
        holder.affectedApps.setText(holder.view.getContext().getString(R.string.permissions_number_apps, data.getPermissionStatusList().size()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView permissionName;
        final TextView permissionSimpleName;
        final TextView affectedApps;

        ViewHolder(View v) {
            super(v);
            view = v;
            permissionName = v.findViewById(R.id.permission_name);
            permissionSimpleName = v.findViewById(R.id.permission_simple_name);
            affectedApps = v.findViewById(R.id.affected_apps);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PermissionDetailActivity.class);
                    intent.putExtra(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA, getItem(getAdapterPosition()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}