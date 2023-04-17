package sk.styk.martin.apkanalyzer.util.bindingadapters

import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout

@BindingAdapter("onOffsetChangeListener")
fun AppBarLayout.setOnOffsetChangeListener(listener: AppBarLayout.OnOffsetChangedListener) {
    addOnOffsetChangedListener(listener)
}
