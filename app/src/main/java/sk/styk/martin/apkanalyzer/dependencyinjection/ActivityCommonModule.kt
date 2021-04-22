package sk.styk.martin.apkanalyzer.dependencyinjection

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.manager.analytics.AnalyticsTracker
import sk.styk.martin.apkanalyzer.manager.analytics.FragmentScreenTracker
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionsManagerImpl
import sk.styk.martin.apkanalyzer.manager.resources.ActivityColorThemeManager
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager

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
        val permissionsManager: PermissionsManagerImpl = ViewModelProvider(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PermissionsManagerImpl(activity.applicationContext) as T
            }
        }).get(PermissionsManagerImpl::class.java)
        permissionsManager.bind(activity)
        return permissionsManager
    }

    @Provides
    @ActivityScoped
    fun provideColorThemeManager(activity: AppCompatActivity, @ForApplication applicationColorThemeManager: ColorThemeManager): ActivityColorThemeManager {
        val activityColorThemeManager: ActivityColorThemeManager = ViewModelProvider(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ActivityColorThemeManager(applicationColorThemeManager) as T
            }
        }).get(ActivityColorThemeManager::class.java)
        activityColorThemeManager.bind(activity)
        return activityColorThemeManager
    }

    @Provides
    @ActivityScoped
    fun provideFragmentScreenTracker(activity: AppCompatActivity, analyticsTracker: AnalyticsTracker): FragmentScreenTracker {
        val fragmentScreenTracker: FragmentScreenTracker = ViewModelProvider(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FragmentScreenTracker(analyticsTracker) as T
            }
        }).get(FragmentScreenTracker::class.java)
        fragmentScreenTracker.bind(activity)
        return fragmentScreenTracker
    }

}