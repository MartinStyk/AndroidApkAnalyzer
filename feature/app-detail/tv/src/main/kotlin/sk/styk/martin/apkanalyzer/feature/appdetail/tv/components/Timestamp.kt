package sk.styk.martin.apkanalyzer.feature.appdetail.tv.components

import java.text.DateFormat
import java.time.Instant
import java.util.Date

internal fun formatTimestamp(instant: Instant): String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date.from(instant))

internal fun formatDate(instant: Instant): String = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date.from(instant))
