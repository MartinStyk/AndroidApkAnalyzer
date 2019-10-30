package sk.styk.martin.apkanalyzer.ui.base

/**
 * Interface representing a BasePresenter in a model view pagerPresenter (MVP) pattern.
 */
interface BasePresenter<View> {

    var view: View

    /**
     * Called to initialize pagerPresenter
     */
    fun initialize() {}

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    fun resume() {}

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    fun pause() {}

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    fun destroy() {}
}