package sk.styk.martin.apkanalyzer.business.upload.task

import android.os.AsyncTask
import sk.styk.martin.apkanalyzer.business.upload.logic.AppDataUploadService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData

/**
 * Background execution place for uploading single app data to server.
 *
 * @author Martin Styk
 * @version 6.11.2017.
 */
class AppDataUploadTask : AsyncTask<AppDetailData?, Void, Void>() {

    public override fun doInBackground(vararg inputData: AppDetailData?): Void? {
        val data = inputData[0] ?: return null

        AppDataUploadService().upload(data)

        return null
    }
}