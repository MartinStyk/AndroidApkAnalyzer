package sk.styk.martin.apkanalyzer.business.upload.task

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.Trigger
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppBasicDataService
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppDetailDataService
import sk.styk.martin.apkanalyzer.business.database.service.SendDataService
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper

/**
 * Job for uploading all non-system apps to server.
 *
 * Because of changes introduced in API 26, JobService is required. It starts thread which triggers
 * AppDataUploadTask for each app.
 *
 * Uploads all not uploaded not pre-installed apps to server.
 * Starts only if internet connection is available.
 * Uploads only previously not uploaded data.
 *
 * @author Martin Styk
 * @version 08.11.2017.
 */
class MultipleAppDataUploadJob : JobService() {

    private val task: Thread = object : Thread() {
        override fun run() {
            Log.i(MultipleAppDataUploadJob::class.java.name, "Upload of all apps was triggered")

            if (!ConnectivityHelper.isUploadPossible(applicationContext)) {
                jobFinished(jobParameters, false)
                return
            }

            val apps = AppBasicDataService(packageManager).getForSources(false, AppSource.AMAZON_STORE, AppSource.GOOGLE_PLAY, AppSource.UNKNOWN)
            val detailDataService = AppDetailDataService(packageManager)

            for (app in apps) {

                if (!SendDataService.isAlreadyUploaded(app.packageName, app.version, applicationContext)) {
                    val appDetailData = detailDataService.get(app.packageName, null)
                    AppDataUploadTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, appDetailData)
                }

                if (isInterrupted)
                    break

            }
            jobFinished(jobParameters, false)
        }
    }

    private lateinit var jobParameters: JobParameters

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        this.jobParameters = jobParameters
        task.start()
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        if (task.isAlive)
            task.interrupt()
        return false
    }

    companion object {

        /**
         * Schedules upload of all apps to server. Upload will start sometimes during next 5 minutes.
         */
        fun start(context: Context) {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))

            val myJob = dispatcher.newJobBuilder()
                    .setService(MultipleAppDataUploadJob::class.java)
                    .setTag(MultipleAppDataUploadJob::class.java.name)
                    .setRecurring(false)
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    // start between 0 and 60 seconds from now
                    .setTrigger(Trigger.executionWindow(0, 300))
                    // don't overwrite an existing job with the same tag
                    .setReplaceCurrent(false)
                    // constraints that need to be satisfied for the job to run
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build()

            dispatcher.mustSchedule(myJob)
        }
    }
}


