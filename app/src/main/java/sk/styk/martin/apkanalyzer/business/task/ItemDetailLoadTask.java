package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.InstalledAppsService;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * Async task for loading detailed info about application
 * <p>
 *
 * TODO now in demo mode - loads only AppBasicInfo
 * Created by Martin Styk on 15.06.2017.
 */
public class ItemDetailLoadTask extends AsyncTask<Object, Void, AppBasicInfo> {

    private Context context;
    private Callback callback;

    public interface Callback {
        void onTaskCompleted(AppBasicInfo data);

        void onTaskStart();
    }

    public ItemDetailLoadTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected AppBasicInfo doInBackground(Object... params) {
        // TODO this is super badass approach - just demo
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int numberOfApp = (Integer) params[0];
        return new InstalledAppsService(context).getAll().get(numberOfApp);
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        callback.onTaskStart();
    }


    @Override
    protected void onPostExecute(AppBasicInfo data) {
        super.onPostExecute(data);
        callback.onTaskCompleted(data);
    }

}
