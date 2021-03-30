package sk.styk.martin.apkanalyzer.dependencyinjection.activity

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ActivityScope
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionsManagerImpl

@Module
class ActivityCommonModule {

    @Provides
    @ActivityScope
    fun provideActivityContext(activity: Activity): Context = activity.baseContext

    @Provides
    @ActivityScope
    fun providesResources(activity: Activity): AppCompatActivity = activity as AppCompatActivity

    @Provides
    @ActivityScope
    fun providePermissionsManager(activity: AppCompatActivity): PermissionManager {
        val permissionsManager: PermissionsManagerImpl = ViewModelProvider(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PermissionsManagerImpl(activity.applicationContext) as T
            }
        }).get(PermissionsManagerImpl::class.java)
        permissionsManager.bind(activity)
        return permissionsManager
    }

//    @Provides
//    @ActivityScope
//    fun provideBackPressedManager(backPressManager: BackPressedManager): BackPressedManager = backPressManager

}