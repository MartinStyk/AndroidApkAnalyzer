package sk.styk.martin.apkanalyzer

import android.app.Application
import android.content.Context
import android.os.StrictMode

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