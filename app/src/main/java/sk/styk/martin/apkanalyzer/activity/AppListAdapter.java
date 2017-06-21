package sk.styk.martin.apkanalyzer.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * Adapter for list of applications
 */
public class AppListAdapter extends ArrayAdapter<AppBasicInfo> {
    private final LayoutInflater mInflater;

    public AppListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<AppBasicInfo> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    /**
     * Populate new items in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.app_list_content, parent, false);
        } else {
            view = convertView;
        }

        AppBasicInfo item = getItem(position);
        ((ImageView)view.findViewById(R.id.package_img)).setImageDrawable(item.getIcon());
        ((TextView)view.findViewById(R.id.application_name)).setText(item.getApplicationName());
        ((TextView)view.findViewById(R.id.package_name)).setText(item.getPackageName());

        return view;
    }
}