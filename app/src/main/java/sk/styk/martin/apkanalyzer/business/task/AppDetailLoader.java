package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;

import sk.styk.martin.apkanalyzer.business.service.InstalledAppsService;
import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * Loader async task for item for AppDetailFragment and AppDetailActivity
 * <p>
 * Created by Martin Styk on 15.06.2017.
 */
public class AppDetailLoader extends AsyncTaskLoader<AppBasicInfo> {
    private final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();

    private final InstalledAppsService installedAppsService;

    private AppBasicInfo item;

    private int numberOfApp;

    public AppDetailLoader(Context context, int numberOfApp) {
        super(context);
        this.numberOfApp = numberOfApp;
        installedAppsService = new InstalledAppsService(context.getPackageManager());
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override
    public AppBasicInfo loadInBackground() {
        return installedAppsService.getAll().get(numberOfApp);
    }


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(AppBasicInfo app) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (app != null) {
                onReleaseResources(app);
            }
        }
        AppBasicInfo oldApp = item;
        item = app;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(item);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldApp != null) {
            onReleaseResources(oldApp);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (item != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(item);
        }

        // Has something interesting in the configuration changed since we
        // last built the app list?
        boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

        if (takeContentChanged() || item == null || configChange) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(AppBasicInfo app) {
        super.onCanceled(app);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(app);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (item != null) {
            onReleaseResources(item);
            item = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(AppBasicInfo apps) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

    /**
     * Helper for determining if the configuration has changed in an interesting
     * way so we need to rebuild the app list.
     */
    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        boolean applyNewConfig(Resources res) {
            int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
            boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
            if (densityChanged || (configChanges & (ActivityInfo.CONFIG_LOCALE
                    | ActivityInfo.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
                mLastDensity = res.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }
}

