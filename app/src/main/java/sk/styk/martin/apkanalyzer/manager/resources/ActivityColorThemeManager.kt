package sk.styk.martin.apkanalyzer.manager.resources

import android.app.Activity
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class ActivityColorThemeManager @Inject constructor(private val colorThemeManager: ColorThemeManager) : ViewModel(), ColorThemeManager by colorThemeManager {

    private var activity: WeakReference<Activity>? = null

    fun bind(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    fun isNightMode(): Boolean {
        val uiMode = activity?.get()?.resources?.configuration?.uiMode ?: return false
        return uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}
