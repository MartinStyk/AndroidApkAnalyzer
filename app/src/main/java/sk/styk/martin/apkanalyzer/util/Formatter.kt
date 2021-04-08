package sk.styk.martin.apkanalyzer.util

import android.content.Context
import android.text.format.DateUtils
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import javax.inject.Inject
import android.text.format.Formatter as AndroidFormatter


class Formatter @Inject constructor(@ForApplication val context: Context) {

    fun formatShortFileSize(size: Long) : String = AndroidFormatter.formatShortFileSize(context, size)

    fun formatDateTime(millis: Long, flags: Int) : String = DateUtils.formatDateTime(context, millis, flags)


}