package sk.styk.martin.apkanalyzer.dependencyinjection

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.review.ReviewManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import sk.styk.martin.apkanalyzer.manager.analytics.AnalyticsTracker
import sk.styk.martin.apkanalyzer.manager.analytics.FragmentScreenTracker
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.ForegroundFragmentWatcher
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionsManagerImpl
import sk.styk.martin.apkanalyzer.manager.promo.UserReviewManager
import sk.styk.martin.apkanalyzer.manager.resources.ActivityColorThemeManager
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider

@InstallIn(ActivityComponent::class)
@Module
class ActivityCommonModule {

    @Provides
    @ActivityScoped
    fun provideActivityContext(activity: Activity): Context = activity.baseContext

    @Provides
    @ActivityScoped
    fun providesResources(activity: Activity): AppCompatActivity = activity as AppCompatActivity

    @Provides
    @ActivityScoped
    fun providePermissionsManager(activity: AppCompatActivity): PermissionManager {
        val permissionsManager: PermissionsManagerImpl = ViewModelProvider(
            activity,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return PermissionsManagerImpl(activity.application) as T
                }
            },
        )[PermissionsManagerImpl::class.java]
        permissionsManager.bind(activity)
        return permissionsManager
    }

    @Provides
    @ActivityScoped
    fun provideColorThemeManager(activity: AppCompatActivity, applicationColorThemeManager: ColorThemeManager): ActivityColorThemeManager {
        val activityColorThemeManager: ActivityColorThemeManager =
            ViewModelProvider(
                activity,
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return ActivityColorThemeManager(applicationColorThemeManager) as T
                    }
                },
            )[ActivityColorThemeManager::class.java]
        activityColorThemeManager.bind(activity)
        return activityColorThemeManager
    }

    @Provides
    @ActivityScoped
    fun provideFragmentScreenTracker(activity: AppCompatActivity, analyticsTracker: AnalyticsTracker, foregroundWatcher: ForegroundFragmentWatcher, dispatcherProvider: DispatcherProvider): FragmentScreenTracker {
        return ViewModelProvider(
            activity,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return FragmentScreenTracker(foregroundWatcher, analyticsTracker, dispatcherProvider) as T
                }
            },
        )[FragmentScreenTracker::class.java]
    }

    @Provides
    @ActivityScoped
    fun provideForegroundFragmentWatcher(activity: AppCompatActivity): ForegroundFragmentWatcher {
        val foregroundFragmentWatcher: ForegroundFragmentWatcher =
            ViewModelProvider(
                activity,
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return ForegroundFragmentWatcher() as T
                    }
                },
            )[ForegroundFragmentWatcher::class.java]
        foregroundFragmentWatcher.bind(activity)
        return foregroundFragmentWatcher
    }

    @Provides
    @ActivityScoped
    fun provideUserReviewManager(activity: AppCompatActivity, reviewManager: ReviewManager): UserReviewManager {
        val userReviewmanager: UserReviewManager = ViewModelProvider(
            activity,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return UserReviewManager(reviewManager) as T
                }
            },
        )[UserReviewManager::class.java]
        userReviewmanager.bind(activity)
        return userReviewmanager
    }
}
