package sk.styk.martin.apkanalyzer.util.file

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * @author Martin Styk
 * @version 27.01.2018.
 */
object ClipBoardHelper {

    fun copyToClipBoard(ctx: Context, text: String, label: String = "label") {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText(label, text)
    }
}