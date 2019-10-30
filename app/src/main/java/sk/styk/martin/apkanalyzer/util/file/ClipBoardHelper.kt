package sk.styk.martin.apkanalyzer.util.file

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipBoardHelper {

    fun copyToClipBoard(ctx: Context, text: String, label: String = "label") {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    }
}