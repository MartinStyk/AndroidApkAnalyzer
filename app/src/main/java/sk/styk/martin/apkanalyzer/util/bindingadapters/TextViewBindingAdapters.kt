package sk.styk.martin.apkanalyzer.util.bindingadapters

import android.widget.TextView
import androidx.annotation.PluralsRes
import androidx.databinding.BindingAdapter
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.TextInfo

@BindingAdapter("pluralRes", "pluralCount")
fun TextView.setPluralString(@PluralsRes pluralRes: Int, pluralCount: Int) {
    text = context.resources.getQuantityString(R.plurals.permissions_number_apps, pluralCount, pluralCount)
}

@BindingAdapter("text")
fun TextView.setText(textInfo: TextInfo?) {
    text = textInfo?.getText(context) ?: ""
}
