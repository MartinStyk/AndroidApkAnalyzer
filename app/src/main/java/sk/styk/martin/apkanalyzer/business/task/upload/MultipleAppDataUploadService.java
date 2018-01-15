package sk.styk.martin.apkanalyzer.business.task.upload;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.business.service.AppDetailDataService;
import sk.styk.martin.apkanalyzer.database.service.SendDataService;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.detail.AppSource;
import sk.styk.martin.apkanalyzer.model.list.AppListData;
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper;

/**
 * Job for uploading all non-system apps to server.
 * <p>
 * Because of changes introduced in API 26, JobService is required. It starts thread which triggers
 * AppDataUploadTask for each app.
 * <p>
 * Created by mstyk on 11/8/17.
 */
public class MultipleAppDataUploadService extends JobService {

    private Thread task;

    /**
     * Schedules upload of all apps to server. Upload will start sometimes during next 5 minutes.
     *
     * @param context
     */
    public static void start(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job myJob = dispatcher.newJobBuilder()
                .setService(MultipleAppDataUploadService.class)
                .setTag(MultipleAppDataUploadService.class.getName())
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 300))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // constraints that need to be satisfied for the job to run
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        task = createWorkThread(jobParameters);
        task.start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (task != null && task.isAlive()) {
            task.interrupt();
        }
        return false;
    }

    /**
     * Uploads all not uploaded not pre-installed apps to server.
     * Starts only if internet connection is available.
     * Uploads only previously not uploaded data.
     */
    private Thread createWorkThread(final JobParameters parameters) {
        return new Thread() {
            @Override
            public void run() {
                Log.i(MultipleAppDataUploadService.class.getName(), "Upload of all apps was triggered");

                if (!ConnectivityHelper.isUploadPossible(getApplicationContext())){
                    jobFinished(parameters, false);
                    return;
                }

                List<AppListData> apps = new AppBasicDataService(getPackageManager()).getForSources(false, AppSource.AMAZON_STORE, AppSource.GOOGLE_PLAY, AppSource.UNKNOWN);
                AppDetailDataService detailDataService = new AppDetailDataService(getPackageManager());

                for (AppListData app : apps) {

                    if (!SendDataService.isAlreadyUploaded(app.getPackageName(), app.getVersion(), getApplicationContext())) {
                        AppDetailData appDetailData = detailDataService.get(app.getPackageName(), null);
                        AppDataUploadTask appDataSaveTask = new AppDataUploadTask(getApplicationContext());
                        appDataSaveTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, appDetailData);
                    }

                    if (isInterrupted())
                        break;

                }
                jobFinished(parameters, false);
            }
        };
    }
}


