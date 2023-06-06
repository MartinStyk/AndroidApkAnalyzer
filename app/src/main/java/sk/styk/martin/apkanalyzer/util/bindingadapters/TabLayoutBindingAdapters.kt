package sk.styk.martin.apkanalyzer.util.bindingadapters

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.google.android.material.tabs.TabLayout
import sk.styk.martin.apkanalyzer.core.uilibrary.ColorInfo

@BindingAdapter("color")
fun TabLayout.setColor(@ColorInt color: Int) {
    setSelectedTabIndicatorColor(color)
    setTabTextColors(ColorInfo.TEXT_SECONDARY.toColorInt(context), color)
}
