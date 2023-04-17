package sk.styk.martin.apkanalyzer.util.bindingadapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("srcAsync")
fun ImageView.srcAsync(drawable: Drawable?) {
    drawable?.let {
        Glide.with(this)
            .load(drawable)
            .into(this)
    }
}
