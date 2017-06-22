package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.business.service.AppDetailDataService;
import sk.styk.martin.apkanalyzer.model.AppBasicData;
import sk.styk.martin.apkanalyzer.model.AppDetailData;

/**
 * Loader async task for item for AppDetailFragment and AppDetailActivity
 * <p>
 * Created by Martin Styk on 15.06.2017.
 */
public class AppDetailLoader extends AsyncTaskLoader<AppDetailData> {
    private final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();

    private final AppDetailDataService appDetailDataService;

    private AppDetailData item;

    private String packageName;

    public AppDetailLoader(Context context, String packageName) {
        super(context);
        this.packageName = packageName;
        appDetailDataService = new AppDetailDataService(context.getPackageManager());
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override
    public AppDetailData loadInBackground() {
        try {
            Thread.sleep(1000); // todo simulate long laoding, remove when not needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return appDetailDataService.get(packageName);
    }


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(AppDetailData app) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (app != null) {
                onReleaseResources(app);
            }
        }
        AppDetailData oldApp = item;
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
    public void onCanceled(AppDetailData app) {
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
    protected void onReleaseResources(AppDetailData apps) {
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

