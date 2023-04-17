package sk.styk.martin.apkanalyzer.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import sk.styk.martin.apkanalyzer.databinding.ViewLoadingBarBinding

class LoadingBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    private val binding = ViewLoadingBarBinding.inflate(LayoutInflater.from(context), this, true)

    fun setProgress(currentProgress: Int, maxProgress: Int) {
        if (binding.loadingProgressBar.max != maxProgress) {
            binding.loadingProgressBar.max = maxProgress
        }

        binding.loadingProgressBar.progress = currentProgress
    }
}

@BindingAdapter("currentProgress", "maxProgress")
fun LoadingBarView.setProgress(currentProgress: Int?, maxProgress: Int?) {
    if (currentProgress != null && maxProgress != null) {
        this.setProgress(currentProgress, maxProgress)
    }
}
