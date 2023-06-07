package sk.styk.martin.apkanalyzer.dependencyinjection

import android.app.Application
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.preference.PreferenceManager
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManagerImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContentResolver(application: Application): ContentResolver = application.contentResolver

    @Provides
    @Singleton
    fun providePersistenceSharedPreferences(@ApplicationContext context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun provideColorThemeManagerImpl(colorThemeManagerImpl: ColorThemeManagerImpl): ColorThemeManager = colorThemeManagerImpl

    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    @Provides
    fun provideReviewManager(@ApplicationContext context: Context): ReviewManager = ReviewManagerFactory.create(context)
}
