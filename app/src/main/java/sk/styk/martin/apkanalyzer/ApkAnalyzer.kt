package sk.styk.martin.apkanalyzer

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.squareup.leakcanary.LeakCanary

import sk.styk.martin.apkanalyzer.business.upload.task.MultipleAppDataUploadJob
import sk.styk.martin.apkanalyzer.util.FirstStartHelper

/**
 * @author Martin Styk
 * @version 30.10.2017.
 */
class ApkAnalyzer : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)


        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        if (!FirstStartHelper.isFirstStart(applicationContext))
            MultipleAppDataUploadJob.start(applicationContext)
    }

    companion object {

        private lateinit var instance: ApkAnalyzer

        val context: Context
            get() = instance.applicationContext
    }
}