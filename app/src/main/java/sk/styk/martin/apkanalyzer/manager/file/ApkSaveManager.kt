package sk.styk.martin.apkanalyzer.manager.file

import androidx.annotation.IntRange
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import sk.styk.martin.apkanalyzer.manager.notification.NotificationManager
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

private const val SUBDIRECTORY = "exported-apps"

@Singleton
class ApkSaveManager @Inject constructor(
        private val fileManager: FileManager,
        private val notificationManager: NotificationManager,
        private val dispatcherProvider: DispatcherProvider) {

    sealed class AppSaveStatus(open val outputFile: File) {
        data class Progress(@IntRange(from = 0, to = 100) val currentProgress: Int, override val outputFile: File) : AppSaveStatus(outputFile)
        data class Done(override val outputFile: File) : AppSaveStatus(outputFile)
    }

    suspend fun saveApk(appName: String, sourceFile: File, targetFileName: String) {
        GlobalScope.launch {
            try {
                saveApkInternal(appName, sourceFile, targetFileName).collect {
                    val notificationBuilder = notificationManager.showAppExportProgressNotification(appName)
                    when (it) {
                        is AppSaveStatus.Progress -> notificationManager.updateAppExportProgressNotification(notificationBuilder, it.currentProgress)
                        is AppSaveStatus.Done -> notificationManager.showAppExportDoneNotification(appName, it.outputFile)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private suspend fun saveApkInternal(appName: String, sourceFile: File, targetFileName: String) = flow {
        val targetFile = File(fileManager.externalDirectory, "$SUBDIRECTORY/$targetFileName")
        val targetDir = targetFile.parentFile
        if (targetDir?.exists() == false) {
            targetDir.mkdirs()
        }

        emit(AppSaveStatus.Progress(0, targetFile))
        val fileSize = sourceFile.length()

        FileInputStream(sourceFile).use { source ->

            FileOutputStream(targetFile).use { output ->

                val buffer = ByteArray(10240)
                var len: Int = source.read(buffer)
                var readBytes = len
                while (len > 0) {
                    yield()
                    output.write(buffer, 0, len)
                    len = source.read(buffer)
                    readBytes += len
                    emit(AppSaveStatus.Progress((readBytes * 100 / fileSize).toInt(), targetFile))
                }
            }
        }

        emit(AppSaveStatus.Done(targetFile))
    }.flowOn(dispatcherProvider.io())
            .distinctUntilChanged()
            .debounce(1500)

}