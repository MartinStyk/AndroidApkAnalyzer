package sk.styk.martin.apkanalyzer.manager.navigationdrawer

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.util.toFragmentTag
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class ForegroundFragmentWatcher @Inject constructor() : ViewModel() {

    private var fragmentManager: WeakReference<FragmentManager>? = null

    private val foregroundFragmentFlow = MutableSharedFlow<FragmentTag>()
    val foregroundFragment: Flow<FragmentTag> = foregroundFragmentFlow

    private val lifeCycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            f.tag?.toFragmentTag()?.let {
                GlobalScope.launch {
                    foregroundFragmentFlow.emit(it)
                }
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
