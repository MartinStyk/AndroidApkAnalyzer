package sk.styk.martin.apkanalyzer.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_loading_bar.view.*

/**
 * Loading bar with progress updates
 *
 * @author Martin Styk
 * @version 06.07.2017.
 */
class LoadingBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading_bar, this, true)
        loading_progress_bar.progress = 0
        loading_progress_bar.max = 10
    }

    fun setProgress(currentProgress: Int, maxProgress: Int) {
        if (loading_progress_bar.max != maxProgress)
            loading_progress_bar.max = maxProgress

        loading_progress_bar.progress = currentProgress
    }

}
