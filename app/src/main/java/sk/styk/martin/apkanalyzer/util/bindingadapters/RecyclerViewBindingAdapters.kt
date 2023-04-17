package sk.styk.martin.apkanalyzer.util.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("itemDecoration")
fun RecyclerView.itemDecoration(decorate: Boolean) {
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}
