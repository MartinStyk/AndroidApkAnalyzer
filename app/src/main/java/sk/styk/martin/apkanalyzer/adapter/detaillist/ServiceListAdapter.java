package sk.styk.martin.apkanalyzer.adapter.detaillist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.model.detail.ServiceData;
import sk.styk.martin.apkanalyzer.view.DetailListItemView;

/**
 * Created by Martin Styk on 07.07.2017.
 */
public class ServiceListAdapter extends GenericDetailListAdapter<ServiceData, ServiceListAdapter.ViewHolder> {

    public ServiceListAdapter(@NonNull List<ServiceData> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_service_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ServiceData data = getItem(position);
        holder.name.setText(data.getName());
        holder.permission.setValue(data.getPermission());
        holder.exported.setValue(data.isExported());
        holder.stopWithTask.setValue(data.isStopWithTask());
        holder.singleUser.setValue(data.isSingleUser());
        holder.isolatedProcess.setValue(data.isolatedProcess());
        holder.external.setValue(data.isExternalService());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final DetailListItemView permission;
        final DetailListItemView exported;
        final DetailListItemView stopWithTask;
        final DetailListItemView singleUser;
        final DetailListItemView isolatedProcess;
        final DetailListItemView external;


        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.item_service_name);
            permission = v.findViewById(R.id.item_service_permission);
            exported = v.findViewById(R.id.item_service_exported);
            stopWithTask = v.findViewById(R.id.item_service_stop_with_task);
            singleUser = v.findViewById(R.id.item_service_single_user);
            isolatedProcess = v.findViewById(R.id.item_service_isolated_process);
            external = v.findViewById(R.id.item_service_external_service);

        }
    }
}