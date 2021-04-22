package sk.styk.martin.apkanalyzer.manager.file

import android.content.ContentResolver
import android.net.Uri
import androidx.annotation.IntRange
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import sk.styk.martin.apkanalyzer.manager.notification.NotificationManager
import sk.styk.martin.apkanalyzer.util.TAG_EXPORTS
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(FlowPreview::class)
class ApkSaveManager @Inject constructor(
        private val contentResolver: ContentResolver,
        private val notificationManager: NotificationManager,
        private val dispatcherProvider: DispatcherProvider) {

    sealed class AppSaveStatus(open val outputUri: Uri) {
        data class Progress(@IntRange(from = 0, to = 100) val currentProgress: Int, override val outputUri: Uri) : AppSaveStatus(outputUri)
        data class Done(override val outputUri: Uri) : AppSaveStatus(outputUri)
    }

    suspend fun saveApk(appName: String, sourceFile: File, targetUri: Uri) {
        GlobalScope.launch {
            try {
                saveApkInternal(appName, sourceFile, targetUri)
                        .distinctUntilChanged()
                        .debounce(1500)
                        .collect {
                            val notificationBuilder = notificationManager.showAppExportProgressNotification(appName)
                            when (it) {
                                is AppSaveStatus.Progress -> notificationManager.updateAppExportProgressNotification(notificationBuilder, it.currentProgress)
                                is AppSaveStatus.Done -> notificationManager.showAppExportDoneNotification(appName, it.outputUri)
                            }
                        }
            } catch (e: Exception) {
                Timber.tag(TAG_EXPORTS).e(e, "Saving apk failed failed. Appname=${appName}, sourceFile=${sourceFile}, targetUri=$targetUri")
            }
        }
    }

    private suspend fun saveApkInternal(appName: String, sourceFile: File, targetUri: Uri) = flow {
        emit(AppSaveStatus.Progress(0, targetUri))
        val fileSize = sourceFile.length()

        FileInputStream(sourceFile).use { source ->

            contentResolver.openOutputStream(targetUri)!!.use { output ->

                val buffer = ByteArray(10240)
                var len: Int = source.read(buffer)
                var readBytes = len
                while (len > 0) {
                    yield()
                    output.write(buffer, 0, len)
                    len = source.read(buffer)
                    readBytes += len
                    emit(AppSaveStatus.Progress((readBytes * 100 / fileSize).toInt(), targetUri))
                }
            }
        }

        emit(AppSaveStatus.Done(targetUri))

    }.flowOn(dispatcherProvider.io())


}