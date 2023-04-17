package sk.styk.martin.apkanalyzer.manager.promo

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class UserReviewManager @Inject constructor(private val reviewManager: ReviewManager) : ViewModel() {

    private var activity: WeakReference<Activity>? = null

    fun bind(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    suspend fun openGooglePlayReview() {
        val reviewInfo = reviewManager.requestReview()
        reviewManager.launchReviewFlow(activity?.get()!!, reviewInfo)
    }
}
