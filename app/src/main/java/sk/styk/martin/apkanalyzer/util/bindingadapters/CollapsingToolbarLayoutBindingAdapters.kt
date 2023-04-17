package sk.styk.martin.apkanalyzer.util.bindingadapters

import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import sk.styk.martin.apkanalyzer.util.TextInfo

@BindingAdapter("title")
fun CollapsingToolbarLayout.setTitle(textInfo: TextInfo?) {
    title = textInfo?.getText(context) ?: ""
}
