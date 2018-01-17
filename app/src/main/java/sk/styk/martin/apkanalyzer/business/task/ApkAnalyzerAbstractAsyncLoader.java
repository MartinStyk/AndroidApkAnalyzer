package sk.styk.martin.apkanalyzer.business.task;

/**
 * @author Martin Styk @version 20.06.2017.
 */

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;


/**
 * Abstract parent class for loaders.
 * This is just wrapper around async task loader
 * <p>
 * @author Martin Styk
 * @version 15.06.2017.
 */
abstract class ApkAnalyzerAbstractAsyncLoader<T> extends AsyncTaskLoader<T> {

    T items;

    ApkAnalyzerAbstractAsyncLoader(Context context) {
        super(context);
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override
    public abstract T loadInBackground();


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(T newItems) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (items != null) {
                onReleaseResources(items);
            }
        }
        T oldItems = items;
        items = newItems;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(items);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldItems != null) {
            onReleaseResources(oldItems);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (items != null) {
            deliverResult(items);
        }

        if (takeContentChanged() || items == null) {
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(T param) {
        super.onCanceled(param);

        onReleaseResources(param);
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
        if (items != null) {
            onReleaseResources(items);
            items = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    void onReleaseResources(T param) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

}


