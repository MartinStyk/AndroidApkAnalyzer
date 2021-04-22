package sk.styk.martin.apkanalyzer.manager.analytics

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class FragmentScreenTracker @Inject constructor(private val analyticsTracker: AnalyticsTracker) : ViewModel() {

    private var fragmentManager: WeakReference<FragmentManager>? = null

    private val lifeCycleCallbacks = object: FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            f.tag?.let {
                Log.e("XXX", "fragment $it")
                analyticsTracker.trackScreenView(it)
            }
        }
    }

    fun bind(activity: AppCompatActivity) {
        fragmentManager?.get()?.unregisterFragmentLifecycleCallbacks(lifeCycleCallbacks)
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(lifeCycleCallbacks, true)
        fragmentManager = WeakReference(activity.supportFragmentManager)
    }

    override fun onCleared() {
        super.onCleared()
        fragmentManager?.get()?.unregisterFragmentLifecycleCallbacks(lifeCycleCallbacks)
        fragmentManager = null
    }

}