package sk.styk.martin.apkanalyzer.ui.activity.intro

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import com.google.firebase.analytics.FirebaseAnalytics
import sk.styk.martin.apkanalyzer.util.FirstStartHelper
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper

/**
 * @author Martin Styk
 * @version 13.12.2017.
 */
class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val analyzeAppsSlide = SliderPage()
        analyzeAppsSlide.title = getString(R.string.intro_analyze_apps)
        analyzeAppsSlide.description = getString(R.string.intro_analyze_apps_description)
        analyzeAppsSlide.bgColor = ContextCompat.getColor(this, R.color.accentLight)
        analyzeAppsSlide.imageDrawable = R.drawable.ic_lupa

        val permissionsAppsSlide = SliderPage()
        permissionsAppsSlide.title = getString(R.string.intro_permissions)
        permissionsAppsSlide.description = getString(R.string.intro_permissions_description)
        permissionsAppsSlide.bgColor = ContextCompat.getColor(this, R.color.accentLight)
        permissionsAppsSlide.imageDrawable = R.drawable.ic_permission

        val statisticsAppsSlide = SliderPage()
        statisticsAppsSlide.title = getString(R.string.intro_statistics)
        statisticsAppsSlide.description = getString(R.string.intro_statistics_description)
        statisticsAppsSlide.bgColor = ContextCompat.getColor(this, R.color.accentLight)
        statisticsAppsSlide.imageDrawable = R.drawable.ic_chart

        val uploadAppsSlide = SliderPage()
        uploadAppsSlide.title = getString(R.string.intro_upload)
        uploadAppsSlide.description = getString(R.string.intro_upload_description)
        uploadAppsSlide.bgColor = ContextCompat.getColor(this, R.color.accentLight)
        uploadAppsSlide.imageDrawable = R.drawable.ic_upload

        addSlide(AppIntroFragment.newInstance(analyzeAppsSlide))
        addSlide(AppIntroFragment.newInstance(permissionsAppsSlide))
        addSlide(AppIntroFragment.newInstance(statisticsAppsSlide))
        addSlide(AllowMetadataUploadIntroSlide.newInstance(uploadAppsSlide))

        // Hide Skip/Done button.
        showSkipButton(false)
        isProgressButtonEnabled = true

        setVibrate(true)
        setVibrateIntensity(30)

        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, Bundle())
    }


    override fun onDonePressed(currentFragment: Fragment?) {
        // TODO: fix type mismatch
        super.onDonePressed(currentFragment)

        // save user preferences and trigger data upload if possible
        if (currentFragment is AllowMetadataUploadIntroSlide) {
            val isUploadAllowed = currentFragment.isUploadAllowed
            ConnectivityHelper.setConnectionAllowedByUser(applicationContext, isUploadAllowed)

//   Temporary disable uploads
//            if (isUploadAllowed)
//                MultipleAppDataUploadJob.start(applicationContext)
        }

        FirstStartHelper.setFirstStartFinished(applicationContext)
        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, Bundle())
        finish()
    }

    override fun onBackPressed() {
        // Do nothing
    }
}
