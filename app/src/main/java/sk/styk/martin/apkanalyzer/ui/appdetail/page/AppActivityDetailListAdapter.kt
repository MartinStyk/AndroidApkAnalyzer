package sk.styk.martin.apkanalyzer.ui.appdetail.page

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent

abstract class DetailInfoDescriptionAdapter<VH> : RecyclerView.Adapter<VH>() where VH : RecyclerView.ViewHolder {

    protected val openDescriptionEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val openDescription: LiveData<DetailInfoAdapter.DetailInfo> = openDescriptionEvent

    protected val copyToClipboardEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val copyToClipboard: LiveData<DetailInfoAdapter.DetailInfo> = copyToClipboardEvent

}