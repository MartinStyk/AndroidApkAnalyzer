package sk.styk.martin.apkanalyzer.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R

/**
 * Loading bar with progress updates
 *
 * @author Martin Styk
 * @version 06.07.2017.
 */
class LoadingBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    private val progressBar: ProgressBar

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading_bar, this, true)
        progressBar = getChildAt(1) as ProgressBar
        progressBar.progress = 0
        progressBar.max = 10
    }

    fun setProgress(currentProgress: Int, maxProgress: Int) {
        if (progressBar.max != maxProgress)
            progressBar.max = maxProgress

        progressBar.progress = currentProgress
    }

}
