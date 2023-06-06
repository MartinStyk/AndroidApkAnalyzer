package sk.styk.martin.apkanalyzer.ui.appdetail.page

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent

abstract class DetailInfoDescriptionAdapter<VH> : RecyclerView.Adapter<VH>() where VH : RecyclerView.ViewHolder {

    data class CopyToClipboard(val text: TextInfo, val label: TextInfo = text) {
        companion object {
            fun from(detailInfo: DetailInfoAdapter.DetailInfo) = CopyToClipboard(detailInfo.value, detailInfo.value)
        }
    }

    data class Description(val title: TextInfo, val message: TextInfo) {
        companion object {
            fun from(detailInfo: DetailInfoAdapter.DetailInfo) = Description(detailInfo.name, detailInfo.description)
        }
    }

    protected val openDescriptionEvent = SingleLiveEvent<Description>()
    val openDescription: LiveData<Description> = openDescriptionEvent

    protected val copyToClipboardEvent = SingleLiveEvent<CopyToClipboard>()
    val copyToClipboard: LiveData<CopyToClipboard> = copyToClipboardEvent

    protected fun String?.orNa() : TextInfo = this?.let { TextInfo.from(it) } ?: TextInfo.from(R.string.NA)
    protected fun String?.orNone() : TextInfo = this?.let { TextInfo.from(it) } ?: TextInfo.from(R.string.none)
}
