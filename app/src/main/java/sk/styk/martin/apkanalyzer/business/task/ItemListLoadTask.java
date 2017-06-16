package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.InstalledAppsService;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * Async task for loading recycler view on ItemListFragment
 * <p>
 * Created by Martin Styk on 15.06.2017.
 */
public class ItemListLoadTask extends AsyncTask<Object, Void, List<AppBasicInfo>> {

    private Context context;
    private Callback callback;

    public interface Callback {
        void onTaskCompleted(List<AppBasicInfo> list);

        void onTaskStart();
    }

    public ItemListLoadTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public <T extends Context & Callback> ItemListLoadTask(T context) {
        this.context = context;
        this.callback = context;
    }

    @Override
    protected List<AppBasicInfo> doInBackground(Object... params) {
        try {
            //TODO remove simulate long duration
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        return new InstalledAppsService(context).getAll();
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        callback.onTaskStart();
    }


    @Override
    protected void onPostExecute(List<AppBasicInfo> list) {
        super.onPostExecute(list);
        callback.onTaskCompleted(list);
    }

}
