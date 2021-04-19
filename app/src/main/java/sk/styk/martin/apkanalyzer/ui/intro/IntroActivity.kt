package sk.styk.martin.apkanalyzer.ui.intro

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.model.SliderPage
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.persistence.PersistenceManager
import javax.inject.Inject

class IntroActivity : AppIntro(), HasAndroidInjector {

    @Inject
    lateinit var persistenceManager: PersistenceManager

    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        persistenceManager.isOnboardingRequired = false

        val analyzeAppsSlide = SliderPage().apply {
            title = getString(R.string.intro_analyze_apps)
            description = getString(R.string.intro_analyze_apps_description)
            backgroundColor = ContextCompat.getColor(this@IntroActivity, R.color.accentLight)
            imageDrawable = R.drawable.ic_lupa
        }

        val permissionsAppsSlide = SliderPage().apply {
            title = getString(R.string.intro_permissions)
            description = getString(R.string.intro_permissions_description)
            backgroundColor = ContextCompat.getColor(this@IntroActivity, R.color.accentLight)
            imageDrawable = R.drawable.ic_permission
        }

        val statisticsAppsSlide = SliderPage().apply {
            title = getString(R.string.intro_statistics)
            description = getString(R.string.intro_statistics_description)
            backgroundColor = ContextCompat.getColor(this@IntroActivity, R.color.accentLight)
            imageDrawable = R.drawable.ic_chart
        }

        addSlide(AppIntroFragment.newInstance(analyzeAppsSlide))
        addSlide(AppIntroFragment.newInstance(permissionsAppsSlide))
        addSlide(AppIntroFragment.newInstance(statisticsAppsSlide))

        isIndicatorEnabled = true
        setIndicatorColor(
                selectedIndicatorColor = ContextCompat.getColor(this, R.color.colorWhite),
                unselectedIndicatorColor = ContextCompat.getColor(this, R.color.secondary)
        )

        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, Bundle())
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finishIntro()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finishIntro()
    }

    private fun finishIntro() {
        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, Bundle())
        finish()
    }

}