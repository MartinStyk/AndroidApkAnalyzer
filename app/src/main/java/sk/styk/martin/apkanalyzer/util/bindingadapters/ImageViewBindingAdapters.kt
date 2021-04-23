package sk.styk.martin.apkanalyzer.util.bindingadapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.util.*

@BindingAdapter("srcAsync")
fun ImageView.srcAsync(drawable: Drawable) {
    Glide.with(this)
            .load(drawable)
            .into(this)
    Locale.getDefault()
}