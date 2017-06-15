package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;

import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.InstalledAppsService;

/**
 * Async task for loading recycler view on ItemListFragment
 * <p>
 * First param is context from which async task is called
 * <p>
 * Created by Martin Styk on 15.06.2017.
 */
public class ItemListLoadTask extends AsyncTask<Object, Void, List<ApplicationInfo>> {

    private Context context;
    private OnTaskCompleted callback;

    // callback interface
    public interface OnTaskCompleted {
        void onTaskCompleted(List<ApplicationInfo> list);
    }

    public ItemListLoadTask(Context context, OnTaskCompleted callback) {
        this.context = context;
        this.callback = callback;
    }

    public <T extends Context & OnTaskCompleted> ItemListLoadTask(T context) {
        this.context = context;
        this.callback = context;
    }

    @Override
    protected List<ApplicationInfo> doInBackground(Object... params) {
        try {
            //TODO remove simulate long duration
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        return new InstalledAppsService(context).getAll();
    }

    @Override
    protected void onPostExecute(List<ApplicationInfo> list) {
        super.onPostExecute(list);
        callback.onTaskCompleted(list);
    }

}
