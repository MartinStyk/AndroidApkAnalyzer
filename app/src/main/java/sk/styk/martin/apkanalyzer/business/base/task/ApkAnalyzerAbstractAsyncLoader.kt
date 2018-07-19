package sk.styk.martin.apkanalyzer.business.base.task

import android.content.Context
import android.support.annotation.WorkerThread
import android.support.v4.content.AsyncTaskLoader

/**
 * Abstract parent class for loaders.
 * This is just wrapper around async task loader
 *
 * @author Martin Styk
 * @version 15.06.2017.
 */
abstract class ApkAnalyzerAbstractAsyncLoader<T> internal constructor(context: Context) : AsyncTaskLoader<T>(context) {

    internal var items: T? = null

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @WorkerThread
    abstract override fun loadInBackground(): T


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    override fun deliverResult(newItems: T?) {
        if (isReset) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (items != null) {
                onReleaseResources(items)
            }
        }
        val oldItems = items
        items = newItems

        if (isStarted) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(items)
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldItems != null) {
            onReleaseResources(oldItems)
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    override fun onStartLoading() {
        if (items != null) {
            deliverResult(items!!)
        }

        if (takeContentChanged() || items == null) {
            forceLoad()
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    override fun onStopLoading() {
        cancelLoad()
    }

    /**
     * Handles a request to cancel a load.
     */
    override fun onCanceled(param: T?) {
        super.onCanceled(param)

        onReleaseResources(param)
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    override fun onReset() {
        super.onReset()

        // Ensure the loader is stopped
        onStopLoading()

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (items != null) {
            onReleaseResources(items)
            items = null
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    internal fun onReleaseResources(param: T?) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

}


