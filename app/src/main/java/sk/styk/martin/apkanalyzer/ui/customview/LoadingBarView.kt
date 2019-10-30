package sk.styk.martin.apkanalyzer.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_loading_bar.view.*
import sk.styk.martin.apkanalyzer.R

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
