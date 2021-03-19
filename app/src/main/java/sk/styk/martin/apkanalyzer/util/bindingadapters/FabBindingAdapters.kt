package sk.styk.martin.apkanalyzer.util.bindingadapters

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("show")
fun FloatingActionButton.bindFabIsShown(show: Boolean?) {
    if (show == true && !isShown) {
        show()
    } else if (show == false && isShown) {
        hide()
    }
}
