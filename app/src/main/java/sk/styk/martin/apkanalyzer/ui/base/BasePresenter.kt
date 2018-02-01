package sk.styk.martin.apkanalyzer.ui.base

/**
 * Interface representing a BasePresenter in a model view presenter (MVP) pattern.
 *
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface BasePresenter<View> {

    var view: View

    /**
     * Called to initialize presenter
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